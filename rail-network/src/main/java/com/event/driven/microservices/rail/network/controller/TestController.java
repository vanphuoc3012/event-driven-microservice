package com.event.driven.microservices.rail.network.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TestController implements StompSessionHandler {

    StompSession stompSession = null;
    MessageConverter messageConverter = new ByteArrayMessageConverter();

    MappingJackson2MessageConverter jsonConverter = new MappingJackson2MessageConverter();

    @EventListener(value = ApplicationReadyEvent.class)
    public void connect() {
        ReactorNettyTcpStompClient stompClient = new ReactorNettyTcpStompClient("publicdatafeeds.networkrail.co.uk", 61618);
        stompClient.setMessageConverter(messageConverter);

        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.setLogin("vanphuoc3012@gmail.com");
        stompHeaders.setPasscode("Qy4eAWsNwR24!na");

        try {
            stompSession = stompClient.connect(stompHeaders, this).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Connection failed", e);
        }

    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("Connected, session: {}", session.getSessionId());
        session.subscribe("/topic/TRAIN_MVT_ALL_TOC", this);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Exception, session: {}, payload: {}", session.getSessionId(), payload);
        log.error("Exception", exception);
        log.error("Headers: {}", headers);

        String message = new String(payload);
        log.error("Message: {}", message);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.error("Transport error, session: {}", session.getSessionId());
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return byte[].class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        String msg = new String((byte[]) payload);
        var mapper = jsonConverter.getObjectMapper();
        try {
            var prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(msg));
//            log.info("Received message: {}", prettyJson);

        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON", e);
        }
    }
}

package com.event.driven.microservices.rail.network.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
@Slf4j
public class WebSocketEvent {

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        log.info("Received a new web socket connection, message: {}", event.getMessage());

    }
}

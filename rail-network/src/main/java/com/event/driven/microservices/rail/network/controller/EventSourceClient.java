package com.event.driven.microservices.rail.network.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class EventSourceClient {
    private final MappingJackson2MessageConverter jsonConverter = new MappingJackson2MessageConverter();

    public void consumeEvent() {
        WebClient webClient = WebClient.create("https://stream.wikimedia.org/v2/stream");
        var mapper = jsonConverter.getObjectMapper();
        ParameterizedTypeReference<ServerSentEvent<String>> type
                = new ParameterizedTypeReference<>() {
        };
        Flux<ServerSentEvent<String>> eventStream = webClient.get()
                .uri("/recentchange")
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                (content -> {

                    log.info("Received event: " + content.event() + " data: " + content.data());
                    String value = null;
                    try {
                        if (content.data() != null) {
                            value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(content.data()));
                        }
                    } catch (JsonProcessingException e) {
                        log.error("Error parsing data: " + e.getMessage());
                    }

                    log.info("Received event: " + content.event() + " data: " + value);

                }),
                (error -> log.error("Error receiving SSE: " + error)),
                () -> log.info("Completed!!!"));
    }

}

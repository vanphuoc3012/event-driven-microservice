package com.event.driven.microservices.twitter.to.kafka.service.runner.impl;

import com.event.driven.microservices.app.config.DataStreamToKafkaConfig;
import com.event.driven.microservices.twitter.to.kafka.service.runner.StreamRunner;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.PreDestroy;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class WikimediaKafkaStreamRunner implements StreamRunner {

    private final DataStreamToKafkaConfig dataStreamToKafkaConfig;
    private final WebClient webClient = WebClient.create("https://stream.wikimedia.org/v2/stream");
    private final MappingJackson2MessageConverter jsonConverter = new MappingJackson2MessageConverter();

    @Override
    public void start() {
        log.info("Start stream");
        var mapper = jsonConverter.getObjectMapper();
        ParameterizedTypeReference<ServerSentEvent<String>> type = new ParameterizedTypeReference<>() {
        };

        Flux<ServerSentEvent<String>> eventStream = webClient.get()
                                                             .uri("/recentchange")
                                                             .retrieve()
                                                             .bodyToFlux(type);

        var domainFilter = dataStreamToKafkaConfig.getDomainFilter();
        eventStream.subscribe(
                (content -> {
                    if (Objects.nonNull(content.data())) {
                        try {
                            var jsonNode = mapper.readTree(content.data());
                            var domain = jsonNode.get("server_name").asText();
                            if (Objects.nonNull(domain) && domainFilter.contains(domain)) {
                                log.info(mapper.writerWithDefaultPrettyPrinter()
                                               .writeValueAsString(jsonNode));
                            }
                        } catch (JsonProcessingException e) {
                            log.error("Error processing json", e);
                        }
                    }
                }),
                (error -> {
                    log.error("Error receiving SSE: " + error);
                    error.printStackTrace();
                }),
                () -> log.info("Completed!!!"));
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutdown stream");
    }

}

package com.event.driven.microservices.twitter.to.kafka.service.runner.impl;

import com.event.driven.microservices.app.config.KafkaConfigData;
import com.event.driven.microservices.app.config.WikimediaStreamToKafkaConfigData;
import com.event.driven.microservices.kafka.avro.model.WikimediaRCArvoModel;
import com.event.driven.microservices.kafka.producer.service.KafkaProducer;
import com.event.driven.microservices.twitter.to.kafka.service.dto.WikimediaRecentChangeDto;
import com.event.driven.microservices.twitter.to.kafka.service.runner.StreamRunner;
import com.event.driven.microservices.twitter.to.kafka.service.transformer.WikimediaRCToAvroTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import javax.annotation.PreDestroy;
import java.util.Objects;
import java.util.concurrent.Executors;

@Component
@Slf4j
@RequiredArgsConstructor
public class WikimediaKafkaStreamRunner implements StreamRunner {

    private final WikimediaStreamToKafkaConfigData wikimediaStreamToKafkaConfigData;
    private final WebClient webClient;
    private final KafkaProducer<Long, WikimediaRCArvoModel> kafkaProducer;
    private final WikimediaRCToAvroTransformer transformer;
    private final KafkaConfigData kafkaConfigData;
    private Disposable subscription;

    @Override
    public void start() {
        log.info("Start stream");
        ParameterizedTypeReference<ServerSentEvent<WikimediaRecentChangeDto>> type = new ParameterizedTypeReference<>() {
        };
        var domainFilter = wikimediaStreamToKafkaConfigData.getDomainFilter();
        Flux<ServerSentEvent<WikimediaRecentChangeDto>> eventStream = webClient.get()
                                                                               .uri(wikimediaStreamToKafkaConfigData.getStreamUrl())
                                                                               .retrieve()
                                                                               .bodyToFlux(type)
                                                                               .onErrorContinue(
                                                                                       (throwable, object) -> log.error(
                                                                                               "Error: {}, Object: {}",
                                                                                               throwable, object));

        subscription = eventStream.subscribe((content -> {

            var rcEvent = content.data();
            log.debug("Received event {}", rcEvent);

            if (Objects.isNull(rcEvent)) {
                return;
            }

            Executors.newFixedThreadPool(1);

            var serverName = rcEvent.getServerName();
            if (domainFilter.contains(serverName)) {
                log.info("Filtered rc event: {}", rcEvent);
                var rcArvoModel = transformer.getWikimediaRCAvroFromWikimediaJSONResponse(rcEvent);
                kafkaProducer.send(kafkaConfigData.getTopicName(), rcArvoModel.getId(), rcArvoModel);
            }
        }), (error -> {
            log.error("Error receiving SSE: " + error);
            error.printStackTrace();
            this.start();
        }), () -> log.info("Completed!!!"));
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutdown stream");
        if (Objects.nonNull(subscription)) {
            subscription.dispose();
        }
    }
}

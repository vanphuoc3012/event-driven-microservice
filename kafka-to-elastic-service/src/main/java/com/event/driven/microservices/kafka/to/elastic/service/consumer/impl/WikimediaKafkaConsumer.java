package com.event.driven.microservices.kafka.to.elastic.service.consumer.impl;

import com.event.driven.microservices.app.config.KafkaConfigData;
import com.event.driven.microservices.app.config.KafkaConsumerConfigData;
import com.event.driven.microservices.kafka.admin.client.KafkaAdminClient;
import com.event.driven.microservices.kafka.avro.model.WikimediaRCArvoModel;
import com.event.driven.microservices.kafka.to.elastic.service.consumer.KafkaConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class WikimediaKafkaConsumer implements KafkaConsumer<Long, WikimediaRCArvoModel> {
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConsumerConfigData kafkaConsumerConfigData;
    private final KafkaConfigData kafkaConfigData;

    public WikimediaKafkaConsumer(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                                  KafkaAdminClient kafkaAdminClient, KafkaConsumerConfigData kafkaConsumerConfigData,
                                  KafkaConfigData kafkaConfigData) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
        this.kafkaConfigData = kafkaConfigData;
    }

    @EventListener
    public void onAppStarted(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        log.info("Topics with name {} is ready for operation!", kafkaConfigData.getTopicNamesToCreate().toArray());
        Objects.requireNonNull(
                       kafkaListenerEndpointRegistry.getListenerContainer(kafkaConsumerConfigData.getConsumerGroupId()))
               .start();
    }

    @Override
    @KafkaListener(id = "${kafka-consumer.consumer-group-id}", topics = "${kafka-config.topic-name}")
    public void recieve(@Payload List<WikimediaRCArvoModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<Integer> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info(
                "{} number of messages received with keys {}, partitions {} and offsets {}," + "sending it to " + "elastic: Thread id: {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString(),
                Thread.currentThread().getId());
    }
}

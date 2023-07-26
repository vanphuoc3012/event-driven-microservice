package com.event.driven.microservices.kafka.producer.service.impl;

import com.event.driven.microservices.kafka.avro.model.WikimediaRCArvoModel;
import com.event.driven.microservices.kafka.producer.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class WikimediaKafkaProducer implements KafkaProducer<Long, WikimediaRCArvoModel> {
    private final KafkaTemplate<Long, WikimediaRCArvoModel> kafkaTemplate;

    @Override
    public void send(String topicName, Long key, WikimediaRCArvoModel rcEvent) {
        log.info("Sending to Kafka message= {}", rcEvent);
        ListenableFuture<SendResult<Long, WikimediaRCArvoModel>> kafkaResultFuture = kafkaTemplate.send(
                topicName,
                key,
                rcEvent);

        kafkaResultFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending message {} to topic {}", rcEvent, topicName);
            }

            @Override
            public void onSuccess(SendResult<Long, WikimediaRCArvoModel> sendResult) {
                RecordMetadata metadata = sendResult.getRecordMetadata();
                log.debug("Receive new metadata. Topic: {}. Partition: {}, Offset: {}, Timestamp:" + " {}, at time: {}",
                          metadata.topic(),
                          metadata.partition(),
                          metadata.offset(),
                          metadata.timestamp(),
                          System.nanoTime());
            }
        });
    }

    @PreDestroy
    public void close() {
        if (Objects.nonNull(kafkaTemplate)) {
            log.info("Closing Kafka producer");
            kafkaTemplate.destroy();
        }
    }
}

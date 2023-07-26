package com.event.driven.microservices.kafka.producer.service.impl;

import com.event.driven.microservices.kafka.avro.model.WikimediaRCArvoModel;
import com.event.driven.microservices.kafka.producer.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class WikimediaKafkaProducer implements KafkaProducer<Long, WikimediaRCArvoModel> {
    private KafkaTemplate<Long, WikimediaRCArvoModel> kafkaTemplate;

    @Override
    public void send(String topicName, Long key, WikimediaRCArvoModel rcEvent) {
        CompletableFuture<SendResult<Long, WikimediaRCArvoModel>> kafkaResultFuture =
                kafkaTemplate.send(topicName, key, rcEvent);

        kafkaResultFuture.whenComplete(((sendResult, ex) -> {
            if (Objects.nonNull(ex)) {
                log.error("Error while sending message {} to topic {}", rcEvent, topicName);
            } else {
                RecordMetadata metadata = sendResult.getRecordMetadata();
                log.debug("Receive new metadata. Topic: {}. Partition: {}, Offset: {}, Timestamp:" +
                                  " {}, at time: {}",
                          metadata.topic(),
                          metadata.partition(),
                          metadata.offset(),
                          metadata.timestamp(),
                          System.nanoTime());
            }
        }));
    }

    @PreDestroy
    public void close() {
        if (Objects.nonNull(kafkaTemplate)) {
            log.info("Closing Kafka producer");
            kafkaTemplate.destroy();
        }
    }
}

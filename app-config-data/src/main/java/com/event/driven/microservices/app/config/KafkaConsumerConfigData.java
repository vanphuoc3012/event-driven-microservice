package com.event.driven.microservices.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka-consumer-config")
@Data
public class KafkaConsumerConfigData {
    private String keyDeserializer;
    private String valueDeserializer;
    private String consumerGroupId;
    private String autoOffsetReset;
    private String specificAvroReaderKey;
    private String specificAvroReader;
    private String batchListener;
    private String autoStartup;
    private String concurrencyLevel;
    private String sessionTimeOutMs;
    private String heartBeatIntervalsMs;
    private String maxPollIntervalMs;
    private String maxPollRecords;
    private String maxPartitionFetchBytesDefault;
    private String maxPartitionFetchBytesBoostFactor;
    private Long pollTimeOutMs;
}

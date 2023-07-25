package com.event.driven.microservices.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "data-stream-to-kafka-service")
@Data
public class DataStreamToKafkaConfigData {
    private List<String> dataStreamFilterKeywords;
    private List<String> domainFilter;

}

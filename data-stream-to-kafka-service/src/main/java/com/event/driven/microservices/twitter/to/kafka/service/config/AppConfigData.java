package com.event.driven.microservices.twitter.to.kafka.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "data-stream-to-kafka-service")
@Data
public class AppConfigData {
    private List<String> dataStreamFilterKeywords;
    private List<String> domainFilter;
}

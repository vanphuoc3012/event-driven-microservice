package com.event.driven.microservices.twitter.to.kafka.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "twitter-to-kafka-service")
public class AppConfigData {
    private List<String> twitterKeywords;
}

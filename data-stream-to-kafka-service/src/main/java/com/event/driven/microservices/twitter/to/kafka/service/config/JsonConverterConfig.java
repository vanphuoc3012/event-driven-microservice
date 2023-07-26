package com.event.driven.microservices.twitter.to.kafka.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

@Configuration
public class JsonConverterConfig {

    @Bean
    public MappingJackson2MessageConverter mappingJackson2MessageConverter() {
        var messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setPrettyPrint(true);
        return messageConverter;
    }
}

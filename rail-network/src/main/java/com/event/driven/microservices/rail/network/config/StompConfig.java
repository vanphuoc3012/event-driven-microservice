package com.event.driven.microservices.rail.network.config;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//@Configuration
//@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/TRAIN_MVT_ALL_TOC");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/topic")
                .setSystemLogin("vanphuoc3012@gmail.com")
                .setRelayHost("publicdatafeeds.networkrail.co.uk")
                .setRelayPort(61618)
                .setSystemPasscode("Qy4eAWsNwR24!na");
    }
}

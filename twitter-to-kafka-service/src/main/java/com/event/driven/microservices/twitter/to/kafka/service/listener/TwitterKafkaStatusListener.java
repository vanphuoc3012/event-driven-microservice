package com.event.driven.microservices.twitter.to.kafka.service.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
@Slf4j
public class TwitterKafkaStatusListener {
    @JmsListener(destination = "TRAIN_MVT_ALL_TOC")
    public String receiveMessage(final Message jsonMessage) throws JMSException {
        log.info("Received message {}", jsonMessage);
        return jsonMessage.toString();
    }
}

package com.event.driven.microservices.twitter.to.kafka.service.runner.impl;

import com.event.driven.microservices.twitter.to.kafka.service.config.AppConfigData;
import com.event.driven.microservices.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import com.event.driven.microservices.twitter.to.kafka.service.runner.StreamRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Slf4j
@RequiredArgsConstructor
public class TwitterKafkaStreamRunner implements StreamRunner {

    private final AppConfigData appConfigData;
    private final TwitterKafkaStatusListener twitterKafkaStatusListener;

    @Override
    public void start() {
        log.info("Start stream");

    }

    @PreDestroy
    public void shutdown() {

    }

    private void addFilter() {

//        log.info("Started filtering twitter stream for keywords {}", Arrays.toString(keywords));
    }
}

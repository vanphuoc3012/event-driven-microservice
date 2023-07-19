package com.event.driven.microservices.twitter.to.kafka.service;

import com.event.driven.microservices.twitter.to.kafka.service.config.AppConfigData;
import com.event.driven.microservices.twitter.to.kafka.service.runner.StreamRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
@EnableJms
public class TwitterToKafkaServiceApplication implements CommandLineRunner {
    private final AppConfigData appConfigData;
    private final StreamRunner streamRunner;

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("TwitterToKafkaServiceApplication starting...");
        log.info(appConfigData.getTwitterKeywords()
                              .toString());
        streamRunner.start();
    }
}
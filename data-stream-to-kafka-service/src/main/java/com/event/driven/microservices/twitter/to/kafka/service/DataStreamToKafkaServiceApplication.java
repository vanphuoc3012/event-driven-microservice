package com.event.driven.microservices.twitter.to.kafka.service;

import com.event.driven.microservices.twitter.to.kafka.service.init.StreamInitializer;
import com.event.driven.microservices.twitter.to.kafka.service.runner.StreamRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
@ComponentScan("com.event.driven.microservices")
public class DataStreamToKafkaServiceApplication implements CommandLineRunner {
    private final StreamRunner streamRunner;
    private final StreamInitializer streamInitializer;

    public static void main(String[] args) {
        SpringApplication.run(DataStreamToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("TwitterToKafkaServiceApplication starting...");
        streamInitializer.init();
        streamRunner.start();
    }
}
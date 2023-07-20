package com.event.driven.microservices.rail.network;

import com.event.driven.microservices.rail.network.runner.StompRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class RailNetworkApplication implements CommandLineRunner {
    private final StompRunner stompRunner;

    public static void main(String[] args) {
        SpringApplication.run(RailNetworkApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        stompRunner.start();
    }
}
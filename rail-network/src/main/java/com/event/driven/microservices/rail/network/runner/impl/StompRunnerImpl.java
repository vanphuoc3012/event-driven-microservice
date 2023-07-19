package com.event.driven.microservices.rail.network.runner.impl;

import com.event.driven.microservices.rail.network.runner.StompRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompRunnerImpl implements StompRunner {
    @Override
    public void start() {
        log.info("StompRunnerImpl started");
    }
}

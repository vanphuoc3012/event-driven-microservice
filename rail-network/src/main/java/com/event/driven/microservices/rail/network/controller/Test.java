package com.event.driven.microservices.rail.network.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class Test {

    @MessageMapping("/TRAIN_MVT_ALL_TOC")
    public void test(@Payload String message) {
        System.out.println("test");
    }
}

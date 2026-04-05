package com.example.backend.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaDebugConsumer {

    @KafkaListener(topics = "relics-topic", groupId = "debug-group")
    public void debugConsume(String value) {
        System.out.println("[DEBUG CONSUMER] Received message: " + value);
    }
}
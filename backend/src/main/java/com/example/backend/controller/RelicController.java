package com.example.backend.controller;

import com.example.backend.dto.LastTimestampResponse;
import com.example.backend.model.Relic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/relics/electron-data") // matches Electron requests
public class RelicController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public RelicController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    // Simulated storage for last timestamp per user
    private Instant lastTimestamp = Instant.EPOCH;

    // -----------------------------
    // GET last timestamp
    // -----------------------------
    @GetMapping
    public ResponseEntity<LastTimestampResponse> getLastTimestamp(@RequestParam String username) {
        // TODO: fetch real lastTimestamp per user if needed
        return ResponseEntity.ok(new LastTimestampResponse(lastTimestamp.toString()));
    }

    // -----------------------------
    // POST relics to Kafka
    // -----------------------------
    @PostMapping
    public ResponseEntity<String> sendRelics(
            @RequestParam String username,
            @RequestBody List<Relic> relics) {

        try {
            for (Relic relic : relics) {
                String json = objectMapper.writeValueAsString(relic);
                kafkaTemplate.send("relics-topic", username, json);
            }
            return ResponseEntity.ok("Relics sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send relics to Kafka");
        }
    }
}
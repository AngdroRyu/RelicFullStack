package com.example.backend.controller;

import com.example.backend.model.Relic;
import com.example.backend.model.User;
import com.example.backend.repository.RelicRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.dto.LastTimestampResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/relics")
public class RelicController {

    private final RelicRepository relicRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public RelicController(RelicRepository relicRepository,
            UserRepository userRepository,
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.relicRepository = relicRepository;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    // -----------------------------
    // GET last timestamp
    // -----------------------------
    @GetMapping("/last-timestamp")
    public ResponseEntity<LastTimestampResponse> getLastTimestamp(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.ok(new LastTimestampResponse(null));
        }

        Relic latestRelic = relicRepository.findTopByUserOrderByTimestampDesc(user);
        if (latestRelic == null) {
            return ResponseEntity.ok(new LastTimestampResponse(null));
        }

        String timestampStr = latestRelic.getTimestamp() != null
                ? latestRelic.getTimestamp().toString()
                : null;

        return ResponseEntity.ok(new LastTimestampResponse(timestampStr));
    }

    // -----------------------------
    // POST relics (save to DB + Kafka)
    // -----------------------------
    @PostMapping("/electron-data")
    public ResponseEntity<String> sendRelics(@RequestParam String username,
            @RequestBody List<Relic> relics) {

        System.out.println("\n=== [POST /electron-data] ===");
        System.out.println("Username: " + username);
        System.out.println("Relics received: " + relics.size());

        if (relics == null || relics.isEmpty()) {
            System.err.println("[VALIDATION ERROR] No relics provided");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No relics provided");
        }

        try {
            // Find or create user
            User user = userRepository.findByUsername(username);
            if (user == null) {
                user = new User();
                user.setUsername(username);
                userRepository.save(user);
            }

            // Associate each relic with the user and save to DB
            for (Relic r : relics) {
                r.setUser(user);
                // Ensure timestamp exists
                if (r.getTimestamp() == null) {
                    r.setTimestamp(Instant.now());
                }
                relicRepository.save(r);
            }

            // Serialize relics to JSON
            String json = objectMapper.writeValueAsString(relics);
            System.out.println("[SERIALIZATION SUCCESS] JSON length: " + json.length());

            // Synchronous send to Kafka
            kafkaTemplate.send("relics-topic", username, json).get();
            System.out.println("[KAFKA SEND SUCCESS] Topic: relics-topic, Key: " + username);

            return ResponseEntity.ok("Relics saved and sent successfully");

        } catch (Exception e) {
            System.err.println("[KAFKA/DB FAILURE]");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process relics: " + e.getMessage());
        }
    }
}
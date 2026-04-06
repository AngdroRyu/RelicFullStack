package com.example.backend.controller;

import com.example.backend.model.Relic;
import com.example.backend.model.Substat;
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
import java.util.Optional;
import java.util.UUID;

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
    public ResponseEntity<LastTimestampResponse> getLastTimestamp(@RequestParam String uuid) {
        Optional<User> optionalUser = userRepository.findByUuid(uuid);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.ok(new LastTimestampResponse(null));
        }

        User user = optionalUser.get();

        Relic latestRelic = relicRepository.findTopByUserOrderByTimestampDesc(user);
        String timestampStr = latestRelic != null && latestRelic.getTimestamp() != null
                ? latestRelic.getTimestamp().toString()
                : null;

        return ResponseEntity.ok(new LastTimestampResponse(timestampStr));
    }

    // -----------------------------
    // POST relics (save to DB + Kafka)
    // -----------------------------
    @PostMapping("/electron-data")
    public ResponseEntity<String> sendRelics(
            @RequestParam("uuid") String uuid,
            @RequestBody List<Relic> relics) {

        User user = userRepository.findByUuid(uuid)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUuid(uuid);
                    return userRepository.save(newUser);
                });

        // 2. Save each relic with substats
        for (Relic relic : relics) {
            relic.setUser(user);

            // Make sure substats are correctly linked
            if (relic.getSubstats() != null) {
                for (Substat s : relic.getSubstats()) {
                    s.setRelic(relic);
                }
            }

            relicRepository.save(relic); // CascadeType.ALL will save substats too
        }

        return ResponseEntity.ok("Relics saved successfully");
    }

    // -----------------------------
    // REGISTER USER
    // -----------------------------
    @PostMapping("/register")
    public ResponseEntity<String> register() {
        String uuid = UUID.randomUUID().toString();

        User user = new User();
        user.setUuid(uuid);

        userRepository.save(user);

        return ResponseEntity.ok(uuid);
    }
}
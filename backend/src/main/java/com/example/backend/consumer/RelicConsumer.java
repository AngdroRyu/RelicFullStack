package com.example.backend.consumer;

import com.example.backend.model.User;
import com.example.backend.model.Relic;
import com.example.backend.repository.RelicRepository;
import com.example.backend.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RelicConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final RelicRepository relicRepository;
    private final UserRepository userRepository;

    @Autowired
    public RelicConsumer(RelicRepository relicRepository, UserRepository userRepository) {
        this.relicRepository = relicRepository;
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "relics-topic", groupId = "relics-group")
    public void listen(ConsumerRecord<String, String> record) {
        String message = record.value();

        System.out.println("\n=== [KAFKA MESSAGE RECEIVED] ===");
        System.out.println("Raw message: " + message);

        List<Relic> relics;

        // 🔹 STEP 1: Parse JSON
        try {
            if (message.trim().startsWith("[")) {
                relics = objectMapper.readValue(message, new TypeReference<List<Relic>>() {
                });
            } else {
                Relic single = objectMapper.readValue(message, Relic.class);
                relics = List.of(single);
            }

            System.out.println("[PARSE SUCCESS] Parsed " + relics.size() + " relic(s)");

        } catch (Exception e) {
            System.err.println("[PARSE ERROR] Failed to parse message");
            e.printStackTrace();
            return;
        }

        // 🔹 STEP 2: Resolve User via UUID
        for (Relic r : relics) {
            if (r.getUser() == null || r.getUser().getUuid() == null) {
                System.err.println("[VALIDATION ERROR] Missing user UUID: " + r);
                return;
            }

            Optional<User> optionalUser = userRepository.findByUuid(r.getUser().getUuid());
            if (optionalUser.isEmpty()) {
                System.err.println("[VALIDATION ERROR] No user found with UUID: " + r.getUser().getUuid());
                return;
            }
            User u = optionalUser.get();
            r.setUser(u);

            r.setUser(u);

            // Ensure timestamp exists
            if (r.getTimestamp() == null) {
                r.setTimestamp(java.time.Instant.now());
            }
        }

        // 🔹 STEP 3: Save to DB
        try {
            System.out.println("[DB SAVE] Attempting to save relics...");
            relicRepository.saveAll(relics);
            System.out.println("[DB SAVE SUCCESS] Saved " + relics.size() + " relic(s)");
        } catch (Exception e) {
            System.err.println("[DB SAVE ERROR] Failed to save relics");
            e.printStackTrace();
            return;
        }

        // 🔹 STEP 4: Flush (optional)
        try {
            relicRepository.flush();
            System.out.println("[FLUSH SUCCESS] Changes flushed to DB");
        } catch (Exception e) {
            System.err.println("[FLUSH ERROR] Failed during flush");
            e.printStackTrace();
        }

        // 🔹 STEP 5: Verify data in DB
        try {
            System.out.println("[VERIFY] Checking DB state...");
            for (Relic r : relics) {
                User u = r.getUser();
                List<Relic> dbRelics = relicRepository.findByUser(u);
                System.out.println("User UUID: " + u.getUuid() + " → " + dbRelics.size() + " relic(s)");
                dbRelics.forEach(dr -> System.out.println("  " + dr));
            }
            System.out.println("[VERIFY SUCCESS] Data confirmed in DB");
        } catch (Exception e) {
            System.err.println("[VERIFY ERROR] Failed to query DB");
            e.printStackTrace();
        }

        System.out.println("=== [PROCESS COMPLETE] ===\n");
    }
}
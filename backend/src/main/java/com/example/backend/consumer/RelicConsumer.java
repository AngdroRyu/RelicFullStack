package com.example.backend.consumer;

import com.example.backend.model.User;
import com.example.backend.model.Relic;
import com.example.backend.repository.RelicRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;

@Component
public class RelicConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final RelicRepository relicRepository;

    @Autowired
    public RelicConsumer(RelicRepository relicRepository) {
        this.relicRepository = relicRepository;
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
            return; // stop processing
        }

        // 🔹 STEP 2: Validate data (optional but helpful)
        for (Relic r : relics) {
            if (r.getUser().getUsername() == null) {
                System.err.println("[VALIDATION ERROR] Missing username: " + r);
                return;
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
            return;
        }

        // 🔹 STEP 5: Verify data in DB
        try {
            System.out.println("[VERIFY] Checking DB state...");

            for (Relic r : relics) {
                User u = r.getUser(); // get the User object
                List<Relic> dbRelics = relicRepository.findByUser(u); // query by User
                System.out.println("User: " + u.getUsername() + " → " + dbRelics.size() + " relic(s)");

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
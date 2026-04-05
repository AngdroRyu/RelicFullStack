package com.example.backend.consumer;

import com.example.backend.model.Relic;
import com.example.backend.repository.RelicRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RelicConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RelicRepository relicRepository;

    @Autowired
    public RelicConsumer(RelicRepository relicRepository) {
        this.relicRepository = relicRepository;
    }

    @KafkaListener(topics = "relics-topic", groupId = "relics-group")
    public void listen(ConsumerRecord<String, String> record) {
        String message = record.value();

        try {
            // Deserialize JSON array into a list of Relic objects
            List<Relic> relics = objectMapper.readValue(message, new TypeReference<List<Relic>>() {
            });

            System.out.println("Received " + relics.size() + " relic(s) from Kafka.");

            // Save all relics to the database
            relicRepository.saveAll(relics);
            System.out.println("Saved " + relics.size() + " relic(s) to the database.");

        } catch (Exception e) {
            System.err.println("Failed to parse relics from Kafka message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

// docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --create
// --topic relics-topic --partitions 1 --replication-factor 1
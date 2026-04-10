package com.example.backend.consumer;

import com.example.backend.dto.RelicDTO;
import com.example.backend.dto.SubstatDTO;
import com.example.backend.dto.RelicEventDTO;
import com.example.backend.model.Relic;
import com.example.backend.model.Substat;
import com.example.backend.model.User;
import com.example.backend.repository.RelicRepository;
import com.example.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class RelicConsumer {

    private final ObjectMapper objectMapper;
    private final RelicRepository relicRepository;
    private final UserRepository userRepository;

    public RelicConsumer(
            ObjectMapper objectMapper,
            RelicRepository relicRepository,
            UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.relicRepository = relicRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @KafkaListener(topics = "relics-topic", groupId = "relics-group")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println("🔥 CONSUMER TRIGGERED");
        try {
            String message = record.value();

            // 1. Deserialize Kafka message
            RelicEventDTO event = objectMapper.readValue(message, RelicEventDTO.class);

            String uuid = event.getUuid();
            List<RelicDTO> relicDTOs = event.getRelics();

            // 2. Find or create user
            User user = userRepository.findByUuid(uuid)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setUuid(uuid);
                        return userRepository.save(newUser);
                    });

            // 3. Build batch
            List<Relic> batch = new ArrayList<>();

            for (RelicDTO relicDTO : relicDTOs) {

                Relic relic = new Relic();
                relic.setUser(user);
                relic.setSetName(relicDTO.getSet());
                relic.setPiece(relicDTO.getPiece());
                relic.setSlot(relicDTO.getSlot());
                relic.setMainStat(relicDTO.getMainStat());
                relic.setMainValue(relicDTO.getMainValue());
                relic.setImagePath(relicDTO.getImagePath());

                // timestamp safe parse
                Instant timestamp;
                try {
                    timestamp = relicDTO.getTimestamp() != null
                            ? Instant.parse(relicDTO.getTimestamp())
                            : Instant.now();
                } catch (Exception e) {
                    timestamp = Instant.now();
                }
                relic.setTimestamp(timestamp);

                // substats
                List<Substat> substats = new ArrayList<>();

                if (relicDTO.getSubstats() != null) {
                    for (SubstatDTO sDTO : relicDTO.getSubstats()) {

                        Substat s = new Substat();
                        s.setName(sDTO.getName());
                        s.setValue(sDTO.getValue());
                        s.setRelic(relic);

                        if (sDTO.getRolls() != null && sDTO.getRolls().getBreakdown() != null) {
                            s.setTotalRolls(sDTO.getRolls().getTotalRolls());
                            s.setLowRolls(sDTO.getRolls().getBreakdown().getLow());
                            s.setMedRolls(sDTO.getRolls().getBreakdown().getMed());
                            s.setHighRolls(sDTO.getRolls().getBreakdown().getHigh());
                        }

                        substats.add(s);
                    }
                }

                relic.setSubstats(substats);

                batch.add(relic);
            }

            // 4. Batch insert
            relicRepository.saveAll(batch);

            System.out.println("[KAFKA] Saved " + batch.size() + " relics successfully");

        } catch (Exception e) {
            System.err.println("[KAFKA ERROR]");
            e.printStackTrace();
        }
    }
}
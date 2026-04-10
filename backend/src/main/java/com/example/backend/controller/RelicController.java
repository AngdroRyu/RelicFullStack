package com.example.backend.controller;

import com.example.backend.model.Relic;
import com.example.backend.model.Substat;
import com.example.backend.model.User;
import com.example.backend.repository.RelicRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.dto.LastTimestampResponse;
import com.example.backend.dto.RelicDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import com.example.backend.dto.SubstatDTO;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/relics")
public class RelicController {

    private final RelicRepository relicRepository;
    private final UserRepository userRepository;

    public RelicController(RelicRepository relicRepository,
            UserRepository userRepository,
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.relicRepository = relicRepository;
        this.userRepository = userRepository;

    }

    // -----------------------------
    // GET last timestamp
    // -----------------------------
    @GetMapping("/last-timestamp")
    // ResponseEntity controls the HTTP response, allowing us to set status codes
    // and headers if needed. The body of the response will be a
    // LastTimestampResponse object, which contains the last timestamp as a string.
    // example for response entity
    // /last-timestamp?uuid=123e4567-e89b-12d3-a456-426614174000 @requestparam
    // uuid=123e4567-e89b-12d3-a456-426614174000
    // find by uuid(123e4567-e89b-12d3-a456-426614174000) -> get user -> find top by
    // user order by timestamp desc -> get timestamp -> return as string
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
        // sends 200 OK with body of LastTimestampResponse which contains the timestamp
        // string or null if no relics found for user
        return ResponseEntity.ok(new LastTimestampResponse(timestampStr));
    }

    // -----------------------------
    // POST relics (save to DB + Kafka)
    // -----------------------------
    @PostMapping("/electron-data")
    public ResponseEntity<String> sendRelics(
            @RequestParam("uuid") String uuid,
            // request body is a list of relic DTOs, which will be automatically
            // deserialized from JSON by Spring
            @RequestBody List<RelicDTO> relicDTOs) {

        // 1. Find or create the user
        User user = userRepository.findByUuid(uuid)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUuid(uuid);
                    return userRepository.save(newUser);
                });

        // 2. Save each relic with substats
        for (RelicDTO relicDTO : relicDTOs) {
            Relic relic = new Relic();
            relic.setUser(user);
            relic.setSetName(relicDTO.getSet());
            relic.setPiece(relicDTO.getPiece());
            relic.setSlot(relicDTO.getSlot());
            relic.setMainStat(relicDTO.getMainStat());
            relic.setMainValue(relicDTO.getMainValue());
            relic.setImagePath(relicDTO.getImagePath());

            // Parse timestamp string to Instant
            try {
                if (relicDTO.getTimestamp() != null) {
                    relic.setTimestamp(Instant.parse(relicDTO.getTimestamp()));
                }
            } catch (Exception e) {
                relic.setTimestamp(Instant.now()); // fallback
            }

            // Map substats
            if (relicDTO.getSubstats() != null) {
                for (SubstatDTO sDTO : relicDTO.getSubstats()) {
                    Substat s = new Substat();
                    s.setName(sDTO.getName());
                    s.setValue(sDTO.getValue());
                    s.setRelic(relic);

                    // map rolls
                    if (sDTO.getRolls() != null) {
                        s.setTotalRolls(sDTO.getRolls().getTotalRolls());
                        s.setLowRolls(sDTO.getRolls().getBreakdown().getLow());
                        s.setMedRolls(sDTO.getRolls().getBreakdown().getMed());
                        s.setHighRolls(sDTO.getRolls().getBreakdown().getHigh());
                    }

                    relic.getSubstats().add(s);
                }
            }

            relicRepository.save(relic);
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
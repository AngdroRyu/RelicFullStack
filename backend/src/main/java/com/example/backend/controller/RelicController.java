package com.example.backend.controller;

import com.example.backend.model.Relic;

import com.example.backend.model.User;
import com.example.backend.repository.RelicRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.dto.LastTimestampResponse;
import com.example.backend.dto.RelicDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/relics")
public class RelicController {

    private final RelicRepository relicRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public RelicController(
            UserRepository userRepository,
            RelicRepository relicRepository,
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {

        this.userRepository = userRepository;
        this.relicRepository = relicRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
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
            @RequestBody List<RelicDTO> relicDTOs) {

        try {
            // attach uuid to payload (optional but recommended)
            Map<String, Object> payload = new HashMap<>();
            payload.put("uuid", uuid);
            payload.put("relics", relicDTOs);
            String message = objectMapper.writeValueAsString(payload);

            kafkaTemplate.send("relics-topic", message);

            return ResponseEntity.ok("Sent to Kafka");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send to Kafka");
        }
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
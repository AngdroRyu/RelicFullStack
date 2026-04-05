package com.example.backend.controller;

import com.example.backend.model.Relic;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/relics")
public class RelicController {

    private final Logger logger = LoggerFactory.getLogger(RelicController.class);

    /**
     * Endpoint for Electron app to send relics.
     * Example POST: /api/relics/electron-data?username=player123
     * Body: JSON array of relics
     */
    @PostMapping("/electron-data")
    public ResponseEntity<String> receiveRelics(
            @RequestParam String username,
            @RequestBody List<Relic> relics) {

        if (relics == null || relics.isEmpty()) {
            logger.warn("Received empty relics list from user {}", username);
            return ResponseEntity.badRequest().body("No relics provided");
        }

        // Attach username to each relic
        relics.forEach(r -> r.setUsername(username));

        // For now, just log the relics instead of saving
        relics.forEach(r -> logger.info("Received relic for user {}: {}", username, r));

        logger.info("Received {} relics for user {}", relics.size(), username);
        return ResponseEntity.ok("Received " + relics.size() + " relics for user " + username);
    }
}
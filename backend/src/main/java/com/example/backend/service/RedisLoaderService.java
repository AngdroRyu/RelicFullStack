package com.example.backend.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisLoaderService {

    private final RelicAnalyticsService analyticsService;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisLoaderService(
            RelicAnalyticsService analyticsService,
            RedisTemplate<String, Object> redisTemplate) {
        this.analyticsService = analyticsService;
        this.redisTemplate = redisTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        loadMainStatsToRedis();
    }

    public void loadMainStatsToRedis() {

        List<Object[]> results = analyticsService.getMainStatCounts();

        System.out.println("=== LOADING MAIN STATS INTO REDIS ===");

        for (Object[] row : results) {

            String setName = (String) row[0];
            String slot = (String) row[1];
            String stat = (String) row[2];
            Long count = ((Number) row[3]).longValue();

            String normalizedSet = setName.toLowerCase().replace(" ", "_");

            String normalizedSlot = slot.toLowerCase().replace(" ", "_");

            String key = "relic:" + normalizedSet + ":" + normalizedSlot + ":main_stat";

            System.out.println(
                    "Redis WRITE → Key: " + key +
                            " | Field: " + stat +
                            " | Value: " + count);

            // main data
            redisTemplate.opsForHash().put(key, stat, String.valueOf(count));
            // sets and slots for quick lookup
            redisTemplate.opsForSet().add("relic_sets", normalizedSet);
            redisTemplate.opsForSet().add("relic:" + normalizedSet + ":slots", normalizedSlot);
        }

        System.out.println("=== DONE LOADING REDIS ===");
    }
}
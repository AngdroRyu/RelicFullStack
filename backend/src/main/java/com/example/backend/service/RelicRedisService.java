package com.example.backend.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.backend.model.Relic;

@Service
public class RelicRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RelicRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String normalize(String input) {
        return input == null
                ? "unknown"
                : input.toLowerCase().replace(" ", "_");
    }

    public void updateFromBatch(List<Relic> batch) {

        for (Relic relic : batch) {

            String set = normalize(relic.getSetName());
            String slot = normalize(relic.getSlot());
            String stat = normalize(relic.getMainStat());

            String hashKey = "relic:" + set + ":" + slot + ":main_stat";

            // count stat occurrence
            redisTemplate.opsForHash()
                    .increment(hashKey, stat, 1);

            // count total items in slot
            redisTemplate.opsForValue()
                    .increment("relic:" + set + ":" + slot + ":total", 1);

            // count total items in set
            redisTemplate.opsForValue()
                    .increment("relic:" + set + ":total", 1);

            redisTemplate.opsForSet().add("relic_sets", set);
            redisTemplate.opsForSet().add("relic:" + set + ":slots", slot);
        }
    }

}

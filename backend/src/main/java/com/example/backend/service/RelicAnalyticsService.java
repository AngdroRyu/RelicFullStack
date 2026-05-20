package com.example.backend.service;

import com.example.backend.repository.RelicRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RelicAnalyticsService {

    private final RelicRepository relicRepository;

    public RelicAnalyticsService(RelicRepository relicRepository) {
        this.relicRepository = relicRepository;
    }

    public List<Object[]> getMainStatCounts() {
        return relicRepository.RelicSetSlotMainStatCount();
    }
}
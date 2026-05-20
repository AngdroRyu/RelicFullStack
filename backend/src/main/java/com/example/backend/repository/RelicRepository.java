package com.example.backend.repository;

import com.example.backend.model.Relic;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelicRepository extends JpaRepository<Relic, Long> {

    // Get all relics for a user
    List<Relic> findByUser(User user);

    // Get latest relic
    Relic findTopByUserOrderByTimestampDesc(User user);

    // =========================
    // Mainstat COUNTS BY SET + SLOT
    // =========================
    @Query("""
                SELECT
                    r.setName,
                    r.slot,
                    r.mainStat,
                    COUNT(r)
                FROM Relic r
                GROUP BY r.setName, r.slot, r.mainStat
                ORDER BY r.setName, r.slot, r.mainStat
            """)
    List<Object[]> RelicSetSlotMainStatCount();

    // =========================
    // SUBSTAT COUNTS BY SET + SLOT
    // =========================
    @Query("""
                SELECT
                    r.setName,
                    r.slot,
                    s.name,
                    COUNT(r)
                FROM Relic r
                LEFT JOIN r.substats s
                GROUP BY r.setName, r.slot, s.name
                ORDER BY r.setName, r.slot, s.name
            """)
    List<Object[]> countSubstatsBySlotandSet();
}
package com.example.backend.repository;

import com.example.backend.model.Relic;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelicRepository extends JpaRepository<Relic, Long> {

    // Find all relics belonging to a user
    List<Relic> findByUser(User user);

    // Find the latest relic for a user by timestamp
    Relic findTopByUserOrderByTimestampDesc(User user);
}
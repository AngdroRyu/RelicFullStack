package com.example.backend.repository;

import com.example.backend.model.Relic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelicRepository extends JpaRepository<Relic, Long> {
    List<Relic> findByUsername(String username);
}
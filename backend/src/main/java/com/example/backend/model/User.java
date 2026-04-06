package com.example.backend.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // internal DB id, no need for getter/setter externally

    @Column(unique = true)
    private String username; // optional, for display purposes

    @Column(unique = true, nullable = false)
    private String uuid; // used for login

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Relic> relics;

    public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByUuid(String uuid);
    }

    // ---------- Getters & Setters ----------

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Relic> getRelics() {
        return relics;
    }

    public void setRelics(List<Relic> relics) {
        this.relics = relics;
    }

}
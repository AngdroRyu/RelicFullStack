package com.example.backend.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "relics")
public class Relic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "set_name")
    @JsonProperty("set")
    private String setName;
    private String piece;
    private String slot;
    private String mainStat;
    private String mainValue;
    private String imagePath;

    private Instant timestamp;

    @OneToMany(mappedBy = "relic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Substat> substats = new ArrayList<>();

    // Constructors
    public Relic() {
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getMainStat() {
        return mainStat;
    }

    public void setMainStat(String mainStat) {
        this.mainStat = mainStat;
    }

    public String getMainValue() {
        return mainValue;
    }

    public void setMainValue(String mainValue) {
        this.mainValue = mainValue;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public List<Substat> getSubstats() {
        return substats;
    }

    public void setSubstats(List<Substat> substats) {
        this.substats.clear();
        if (substats != null) {
            for (Substat s : substats) {
                addSubstat(s);
            }
        }
    }

    public void addSubstat(Substat substat) {
        substats.add(substat);
        substat.setRelic(this);
    }

    public void removeSubstat(Substat substat) {
        substats.remove(substat);
        substat.setRelic(null);
    }
}
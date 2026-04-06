package com.example.backend.model;

import jakarta.persistence.*;

@Entity
public class Substat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String value;

    @ManyToOne
    @JoinColumn(name = "relic_id")
    private Relic relic;

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Relic getRelic() {
        return relic;
    }

    public void setRelic(Relic relic) {
        this.relic = relic;
    }
}
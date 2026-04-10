package com.example.backend.model;

import jakarta.persistence.*;

@Entity
public class Substat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String value;

    @Column(name = "total_rolls")
    private Integer totalRolls;

    @Column(name = "low_rolls")
    private Integer lowRolls;

    @Column(name = "med_rolls")
    private Integer medRolls;

    @Column(name = "high_rolls")
    private Integer highRolls;

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

    // NEW GETTERS/SETTERS

    public Integer getTotalRolls() {
        return totalRolls;
    }

    public void setTotalRolls(Integer totalRolls) {
        this.totalRolls = totalRolls;
    }

    public Integer getLowRolls() {
        return lowRolls;
    }

    public void setLowRolls(Integer lowRolls) {
        this.lowRolls = lowRolls;
    }

    public Integer getMedRolls() {
        return medRolls;
    }

    public void setMedRolls(Integer medRolls) {
        this.medRolls = medRolls;
    }

    public Integer getHighRolls() {
        return highRolls;
    }

    public void setHighRolls(Integer highRolls) {
        this.highRolls = highRolls;
    }
}
package com.example.backend.dto;

import java.util.List;

public class RelicDTO {
    private String set;
    private String piece;
    private String slot;
    private String mainStat;
    private String mainValue;
    private String imagePath;
    private String timestamp; // or LocalDateTime if you parse it
    private List<SubstatDTO> substats;

    // getters and setters
    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<SubstatDTO> getSubstats() {
        return substats;
    }

    public void setSubstats(List<SubstatDTO> substats) {
        this.substats = substats;
    }
}
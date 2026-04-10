package com.example.backend.dto;

import java.util.List;

public class RelicEventDTO {

    private String uuid;
    private List<RelicDTO> relics;

    // optional but useful for future scaling
    private String source; // e.g. "electron"
    private String eventId; // for deduplication
    private String timestamp; // event creation time

    // getters and setters

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<RelicDTO> getRelics() {
        return relics;
    }

    public void setRelics(List<RelicDTO> relics) {
        this.relics = relics;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
package com.example.backend.dto;

public class LastTimestampResponse {

    private String lastTimestamp; // ISO 8601 string, e.g., "2026-04-05T05:56:26Z"

    public LastTimestampResponse() {
        // Default constructor needed by Jackson
    }

    public LastTimestampResponse(String lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public String getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(String lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    @Override
    public String toString() {
        return "LastTimestampResponse{" +
                "lastTimestamp='" + lastTimestamp + '\'' +
                '}';
    }
}
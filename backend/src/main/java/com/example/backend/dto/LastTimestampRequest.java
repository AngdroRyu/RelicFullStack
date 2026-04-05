package com.example.backend.dto;

public class LastTimestampRequest {
    private String id; // matches Electron's id field

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
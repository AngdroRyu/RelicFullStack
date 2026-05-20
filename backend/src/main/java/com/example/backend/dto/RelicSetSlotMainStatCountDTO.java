package com.example.backend.dto;

public class RelicSetSlotMainStatCountDTO {

    private String setName;
    private String slot;
    private String mainStat;
    private Long count;

    public RelicSetSlotMainStatCountDTO(String setName, String slot, String mainStat, Long count) {
        this.setName = setName;
        this.slot = slot;
        this.mainStat = mainStat;
        this.count = count;
    }

    public String getSetName() {
        return setName;
    }

    public String getSlot() {
        return slot;
    }

    public String getMainStat() {
        return mainStat;
    }

    public Long getCount() {
        return count;
    }
}
package com.example.backend.dto;

public class SubstatDTO {
    private String name;
    private String value;
    private Rolls rolls;

    // getters and setters
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

    public Rolls getRolls() {
        return rolls;
    }

    public void setRolls(Rolls rolls) {
        this.rolls = rolls;
    }

    // Inner class for rolls
    public static class Rolls {
        private int totalRolls;
        private Breakdown breakdown;

        public int getTotalRolls() {
            return totalRolls;
        }

        public void setTotalRolls(int totalRolls) {
            this.totalRolls = totalRolls;
        }

        public Breakdown getBreakdown() {
            return breakdown;
        }

        public void setBreakdown(Breakdown breakdown) {
            this.breakdown = breakdown;
        }

        public static class Breakdown {
            private int low;
            private int med;
            private int high;

            public int getLow() {
                return low;
            }

            public void setLow(int low) {
                this.low = low;
            }

            public int getMed() {
                return med;
            }

            public void setMed(int med) {
                this.med = med;
            }

            public int getHigh() {
                return high;
            }

            public void setHigh(int high) {
                this.high = high;
            }
        }
    }
}
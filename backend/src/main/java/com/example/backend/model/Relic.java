package com.example.backend.model;

import java.time.Instant;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Relic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key for the database

    @Column(name = "`set`")
    private String set;
    private String piece;
    private String slot;
    private String mainStat;
    private String mainValue;
    private String imagePath;
    private Instant timestamp;
    private String username;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "relic_id") // foreign key in substat table
    private List<Substat> substats;

    public Relic() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Substat> getSubstats() {
        return substats;
    }

    public void setSubstats(List<Substat> substats) {
        this.substats = substats;
    }

    @Override
    public String toString() {
        return "Relic{set='" + set + "', piece='" + piece + "', slot='" + slot + "'}";
    }

    @Entity
    public static class Substat {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private String value;

        @Embedded
        private Rolls rolls;

        public Substat() {
        }

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

        public Rolls getRolls() {
            return rolls;
        }

        public void setRolls(Rolls rolls) {
            this.rolls = rolls;
        }

        @Embeddable
        public static class Rolls {
            private int totalRolls;

            @Embedded
            private Breakdown breakdown;

            public Rolls() {
            }

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

            @Embeddable
            public static class Breakdown {
                private int low;
                private int med;
                private int high;

                public Breakdown() {
                }

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
}
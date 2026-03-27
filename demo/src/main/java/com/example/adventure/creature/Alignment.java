package com.example.adventure.creature;

public enum Alignment {
    LAWFUL_GOOD("Lawful Good", true, false, true, false),
    NEUTRAL_GOOD("Neutral Good", true, false, false, false),
    CHAOTIC_GOOD("Chaotic Good", true, false, false, true), 
    LAWFUL_NEUTRAL("Lawful Neutral", false, false, true, false),
    TRUE_NEUTRAL("True Neutral", false, false, false, false),
    CHAOTIC_NEUTRAL("Chaotic Neutral", false, false, false, true), 
    LAWFUL_EVIL("Lawful Evil", false, true, true, false),
    NEUTRAL_EVIL("Neutral Evil", false, true, false, false),
    CHAOTIC_EVIL("Chaotic Evil", false, true, false, true),
    UNALIGNED("Unaligned", false, false, false, false);

    private final String name;
    private final boolean isGood;
    private final boolean isEvil;
    private final boolean isLawful;
    private final boolean isChaotic;

    Alignment(String name, boolean isGood, boolean isEvil, boolean isLawful, boolean isChoatic) {
        this.name = name;
        this.isGood = isGood;
        this.isEvil = isEvil;
        this.isLawful = isLawful;
        this.isChaotic = isChoatic;
    }
    public String getName() {
        return this.name;
    }
    public boolean isGood() {
        return isGood;
    }
    public boolean isEvil() {
        return this.isEvil;
    }
    public boolean isLawful() {
        return this.isLawful;
    }
    public boolean isChaotic() {
        return this.isChaotic;
    }
    public boolean isOpposed(Alignment other) {
        boolean moralConflict = (this.isGood && other.isEvil) || (this.isEvil && other.isGood);
        boolean orderConflict = (this.isLawful && other.isChaotic) || (this.isChaotic && other.isLawful);
        return moralConflict || orderConflict;
    }
}
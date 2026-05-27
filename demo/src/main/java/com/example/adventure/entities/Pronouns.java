package com.example.adventure.entities;

public enum Pronouns {
    ANDRO("they", "them", "their"),
    FEMALE("she", "her", "hers"),
    MALE("he", "him", "his"),
    XENO("it", "it", "its");

    private final String subjective;
    private final String objective;
    private final String possessive;

    Pronouns(String subjective, String objective, String possessive) {
        this.subjective = subjective;
        this.objective = objective;
        this.possessive = possessive;
    }
    public String subjective() { return subjective; }
    public String objective() { return objective; }
    public String possessive() { return possessive; }
    @Override
    public String toString() {
        return "%s/%s".formatted(subjective, objective);
    }
}
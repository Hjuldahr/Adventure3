package com.example.adventure.entity;

import java.util.EnumMap;
import java.util.Objects;

public class Proficiencies<T extends Enum<T>> {
    
    public enum ProficiencyTiers {
        NONE("○", "Unproficient", 0),
        PROFICIENT("●", "Proficient", 1),
        EXPERT("⦿", "Expert", 2);

        private final String symbol;
        private final String name;
        private final int multiplier;

        ProficiencyTiers(String symbol, String name, int multiplier) {
            this.symbol = symbol;
            this.name = name;
            this.multiplier = multiplier;
        }

        public String getSymbol() { return symbol; }
        public String getName() { return name; }
        public int getMultiplier() { return multiplier; }
    }

    private final EnumMap<T, ProficiencyTiers> categories;
    private final Class<T> typeClass;

    public Proficiencies(Class<T> typeClass) {
        this.typeClass = typeClass;
        this.categories = new EnumMap<>(typeClass);
    }

    /**
     * Copy constructor for creating deep copies of proficiency sets.
     */
    public Proficiencies(Proficiencies<T> other) {
        this.typeClass = other.typeClass;
        this.categories = new EnumMap<>(other.categories);
    }

    /**
     * Fluent API method for chaining initializations.
     */
    public Proficiencies<T> with(T type, ProficiencyTiers tier) {
        setProficiencyTier(type, tier);
        return this;
    }

    public EnumMap<T, ProficiencyTiers> getAll() {
        return categories;
    }

    /**
     * Calculates the raw bonus provided by proficiency.
     * Formula: Proficiency Bonus * Tier Multiplier (0, 1, or 2)
     */
    public int getBonusValue(T type, int characterProficiencyBonus) {
        return characterProficiencyBonus * getProficiencyTier(type).getMultiplier();
    }

    public ProficiencyTiers getProficiencyTier(T type) {
        // Ensures we never return null even if the map doesn't contain the key
        return categories.getOrDefault(type, ProficiencyTiers.NONE);
    }

    public void setProficiencyTier(T type, ProficiencyTiers tier) {
        Objects.requireNonNull(type, "Type cannot be null");
        categories.put(type, tier == null ? ProficiencyTiers.NONE : tier);
    }

    public Class<T> getTypeClass() {
        return typeClass;
    }
}
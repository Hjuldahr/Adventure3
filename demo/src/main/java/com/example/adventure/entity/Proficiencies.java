package com.example.adventure.entity;

import java.util.EnumMap;

public class Proficiencies<T extends Enum<T>> {
    public enum ProficiencyTiers {
        NONE("☆☆", "", 0),
        PROFICIENT("★☆", "Proficient", 2),
        EXPERT("★★", "Expert", 4);

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
    };

    private EnumMap<T,ProficiencyTiers> categories;
    private final Class<T> typeClass;

    public Proficiencies(Class<T> typeClass) {
        this.typeClass = typeClass;
        this.categories = new EnumMap<>(typeClass);
    }
    public Proficiencies(Proficiencies<T> other) {
        this.typeClass = other.typeClass;
        this.categories = new EnumMap<>(other.categories);
    }
    
    public int getProficiencyModifier(T type, int proficiencyBonus) {
        return proficiencyBonus * getProficiencyTier(type).getMultiplier();
    }
    public ProficiencyTiers getProficiencyTier(T type) {
        return categories.getOrDefault(type, ProficiencyTiers.NONE);
    }
    public void setProficiencyTier(T type, ProficiencyTiers tier) {
        categories.put(type, tier);
    }
}
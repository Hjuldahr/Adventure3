package com.example.adventure.entity;

import java.util.EnumMap;

public class Proficiencies<T extends Enum<T>> {
    public enum ProficiencyTiers {
        UNTRAINED("☆☆☆☆", "Untrained", 0),
        TRAINED("☆☆☆★", "Trained", 2),
        EXPERT("★★☆☆", "Expert", 4),
        MASTER("★★★☆", "Master", 6),
        LEGENDARY("★★★★", "Legendary", 8);

        private final String symbol;
        private final String name;
        private final int bonus;

        ProficiencyTiers(String symbol, String name, int bonus) {
            this.symbol = symbol;
            this.name = name;
            this.bonus = bonus;
        }

        public String getSymbol() { return symbol; }
        public String getName() { return name; }
        public int getBonus() { return bonus; }
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
    
    public int getProficiencyModifier(T type, int level) {
        return getProficiencyTier(type).getBonus() + level;
    }
    public ProficiencyTiers getProficiencyTier(T type) {
        return categories.getOrDefault(type, ProficiencyTiers.UNTRAINED);
    }
    public void setProficiencyTier(T type, ProficiencyTiers tier) {
        categories.put(type, tier);
    }
}
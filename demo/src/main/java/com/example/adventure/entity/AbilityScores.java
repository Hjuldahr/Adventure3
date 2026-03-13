package com.example.adventure.entity;

import java.util.EnumMap;

public class AbilityScores {
    public static final int MAXIMUM = 20;
    public static final int MINIMUM = 0;
    public static final int BASELINE = 10;

    public static enum AbilityCategories {
        BRAWN,
        FORTITUDE,
        AGILITY,
        INTELLECT,
        CHARM,
        SPIRIT
    };

    private EnumMap<AbilityCategories,Integer> abilityScores;

    public AbilityScores(int br, int fo, int ag, int in, int ch, int sp) {
        abilityScores = new EnumMap<>(AbilityCategories.class);
        abilityScores.put(AbilityCategories.BRAWN, br);
        abilityScores.put(AbilityCategories.FORTITUDE, fo);
        abilityScores.put(AbilityCategories.AGILITY, ag);
        abilityScores.put(AbilityCategories.INTELLECT, in);
        abilityScores.put(AbilityCategories.CHARM, ch);
        abilityScores.put(AbilityCategories.SPIRIT, sp);
    }

    public AbilityScores(AbilityScores other) {
        abilityScores = new EnumMap<>(other.abilityScores);
    }

    public int getAbilityScore(AbilityCategories category) {
        return abilityScores.getOrDefault(category, BASELINE);
    }

    public void setAbilityScore(AbilityCategories category, int value) {
        abilityScores.put(category, constrainValue(value));
    }

    public void incrementAbilityScore(AbilityCategories category) {
        increaseAbilityScore(category, 1);
    }

    public void increaseAbilityScore(AbilityCategories category, int increase) {
        abilityScores.merge(category, increase, (value, change) -> 
            constrainValue(value + change)
        );
    }

    public void decrementAbilityScore(AbilityCategories category) {
        decreaseAbilityScore(category, 1);
    }

    public void decreaseAbilityScore(AbilityCategories category, int decrease) {
        abilityScores.merge(category, decrease, (value, change) -> 
            constrainValue(value - change)
        );
    }

    private int constrainValue(int value) {
        return Math.clamp(value, MINIMUM, MAXIMUM);
    }

    public int getModifier(AbilityCategories category) {
        int score = abilityScores.getOrDefault(category, BASELINE);
        return Math.floorDiv(score - BASELINE, 2);
    }
}

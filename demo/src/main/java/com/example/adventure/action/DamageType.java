package com.example.adventure.action;

public abstract class DamageType {
    public static enum DamageTypes {
        PHYSICAL, ENCHANTED, SILVERED,
        BURNING, POISONING, CORRODING, FREEZING, 
        PSIONIC, RADIANCE, OBLIVIATING
    };
    public static enum DamageModifierCategories {
        IMMUNITY, // x 0
        RESISTANCE, // x 0.5
        NORMAL, // x 1
        WEAKNESS, // x1.5
        VULNERABILITY // x2
    };
    public static float lookupDamageModifier(DamageModifierCategories category) {
        return switch (category) {
            case RESISTANCE -> 0.5f;
            case IMMUNITY -> 0f;
            case WEAKNESS -> 1.5f;
            case VULNERABILITY -> 2f;
            default -> 1f;
        };
    }
}

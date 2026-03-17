package com.example.adventure.combat;

public abstract class DamageTypeHelper {
    public static enum DamageModifierCategories {
        ABSORPTION, // x -0.5
        IMMUNITY, // x 0
        RESISTANCE, // x 0.5
        NORMAL, // x 1
        WEAKNESS, // x1.5
        VULNERABILITY // x2
    };
    public static float lookupDamageModifier(DamageModifierCategories category) {
        return switch (category) {
            case ABSORPTION -> -0.5f; // take no damage + heal for half
            case RESISTANCE -> 0.5f; 
            case IMMUNITY -> 0f;
            case WEAKNESS -> 1.5f;
            case VULNERABILITY -> 2f;
            default -> 1f;
        };
    }
}

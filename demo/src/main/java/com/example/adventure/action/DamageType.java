package com.example.adventure.action;

import java.util.EnumMap;

public abstract class DamageType {
    public static enum DamageTypes {

    };
    public static enum DamageModifierCategories {
        NORMAL, // x 1
        RESISTANCE, // x 0.5
        IMMUNITY, // x 0
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

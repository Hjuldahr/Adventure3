package com.example.adventure.combat;

import java.util.EnumSet;

public abstract class Conditions {
    public enum ConditionTypes {
        BLINDED,
        CHARMED,
        DEAFENED,
        EXHAUSTION,
        FRIGHTENED,
        GRAPPLED,
        INCAPACITATED,
        INVISIBLE,
        PARALYZED,
        PETRIFIED,
        POISONED,
        PRONE,
        RESTRAINED,
        STUNNED,
        UNCONSCIOUS
    }

    public static final EnumSet<ConditionTypes> FATAL_TYPES = EnumSet.of(
        ConditionTypes.CHARMED, 
        ConditionTypes.INCAPACITATED, 
        ConditionTypes.PARALYZED, 
        ConditionTypes.PETRIFIED, 
        ConditionTypes.STUNNED, 
        ConditionTypes.UNCONSCIOUS
    );
}
package com.example.adventure.entity;

import com.example.adventure.utility.SuccessTypes;

public abstract class RuleResolutionService {
    /**
     * Applies the Incapacitation trait logic.
     * If the target's level is higher than the effect's level, 
     * the degree of success improves by one step.
     */
    public static SuccessTypes applyIncapacitation(SuccessTypes current, int effectLevel, int targetLevel) {
        if (targetLevel > effectLevel) {
            return current.stepUp();
        }
        return current;
    }

    /**
     * Applies D&D 5e-style Evasion.
     * Successes become Critical Successes (no damage).
     */
    public static SuccessTypes applyEvasion(SuccessTypes current) {
        if (current == SuccessTypes.SUCCESS) {
            return SuccessTypes.CRIT_SUCCESS;
        }
        return current;
    }
}
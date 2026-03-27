package com.example.adventure.creature;

import com.example.adventure.utility.SuccessTypes;

public abstract class RuleResolutionService {
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
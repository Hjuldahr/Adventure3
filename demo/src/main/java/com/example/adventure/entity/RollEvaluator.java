package com.example.adventure.entity;

public abstract class RollEvaluator {
    /**
     * Evaluates the base degree of success based on PF2e rules.
     * 
     * @param raw             The value showing on the d20 (1-20).
     * @param total           The final result (raw + modifiers).
     * @param difficultyClass The target number to meet or exceed.
     * @return The resulting SuccessType.
     */
    public static SuccessTypes evaluate(int raw, int total, int difficultyClass) {
        SuccessTypes degree;

        // 1. Determine base degree by numerical difference
        if (total >= difficultyClass + 10) {
            degree = SuccessTypes.CRIT_SUCCESS;
        } else if (total >= difficultyClass) {
            degree = SuccessTypes.SUCCESS;
        } else if (total <= difficultyClass - 10) {
            degree = SuccessTypes.CRIT_FAILURE;
        } else {
            degree = SuccessTypes.FAILURE;
        }

        // 2. Adjust degree based on Natural 20 or Natural 1
        if (raw == 20) {
            degree = degree.stepUp();
        } else if (raw == 1) {
            degree = degree.stepDown();
        }

        return degree;
    }
}
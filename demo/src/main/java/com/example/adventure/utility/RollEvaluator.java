package com.example.adventure.utility;

public abstract class RollEvaluator {
    public static SuccessTypes evaluate(int raw, int total, int difficultyClass) {
        if (raw == 1) return SuccessTypes.CRIT_FAILURE;
        else if (raw == 20) return SuccessTypes.CRIT_SUCCESS;
        else if (total >= difficultyClass) return SuccessTypes.SUCCESS;
        else return SuccessTypes.FAILURE;
    }
}
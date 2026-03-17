package com.example.adventure.utility;

public abstract class Success {
    public static enum SuccessTypes {
        CRIT_FAILURE, FAILURE, SUCCESS, CRIT_SUCCESS
    }
    public static enum LuckTypes {
        FUMBLE, NONE, TRIUMPH
    }

    /**
     * Determines success
     * @param result
     * @param difficultyClass
     * @return
     */
    public static SuccessTypes evaluateSuccess(int result, int difficultyClass) {
        if (result >= difficultyClass + 10) return SuccessTypes.CRIT_SUCCESS;
        if (result >= difficultyClass) return SuccessTypes.SUCCESS;
        if (result < difficultyClass - 10) return SuccessTypes.CRIT_FAILURE;
        return SuccessTypes.FAILURE;
    }

    public static boolean simpleEvaluateSuccess(int result, int difficultyClass) {
        return result >= difficultyClass;
    }

    /**
     * Provides additional luck based bonuses
     * @param raw
     * @return
     */
    public static LuckTypes evaluateLuck(int raw) {
        if (raw == 20) return LuckTypes.TRIUMPH;
        if (raw == 1) return LuckTypes.FUMBLE;
        return LuckTypes.NONE;
    }

    public String categorizeOutcome(SuccessTypes successType, LuckTypes luckType) {
        return switch (luckType) {
            case TRIUMPH -> switch (successType) {
                case CRIT_SUCCESS -> "Exalted Success";
                case SUCCESS -> "Glorious Success";
                case FAILURE -> "Graceful Failure";
                case CRIT_FAILURE -> "Gilded Failure";
            };
            case NONE -> switch (successType) {
                case CRIT_SUCCESS -> "Masterful Success";
                case SUCCESS -> "Success";
                case FAILURE -> "Failure";
                case CRIT_FAILURE -> "Crushing Failure";
            };
            case FUMBLE -> switch (successType) {
                case CRIT_SUCCESS -> "Bumbled Success";
                case SUCCESS -> "Uncertain Success";
                case FAILURE -> "Terrible Failure";
                case CRIT_FAILURE -> "Catastrophic Failure";
            };
        };
    }
}

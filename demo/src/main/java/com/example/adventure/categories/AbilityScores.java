package com.example.adventure.categories;

import java.util.EnumMap;

public class AbilityScores {
    private final int BASE_SCORE = 10;
    private final int MAX_SCORE = 99;
    private final int MIN_SCORE = 1;
    
    private EnumMap<AbilityTypes, Integer> baseScores;
    private EnumMap<AbilityTypes, Integer> scoreBonuses;
    private EnumMap<AbilityTypes, Integer> overrideScores;

    public AbilityScores() {
        baseScores = new EnumMap<>(AbilityTypes.class);
        scoreBonuses = new EnumMap<>(AbilityTypes.class);
        overrideScores = new EnumMap<>(AbilityTypes.class);
    }

    public AbilityScores(AbilityScores other) {
        baseScores = new EnumMap<>(other.baseScores);
        scoreBonuses = new EnumMap<>(other.scoreBonuses);
        overrideScores = new EnumMap<>(other.overrideScores);
    }

    public int getScore(AbilityTypes abilityType) {
        if (overrideScores.containsKey(abilityType)) {
            return overrideScores.get(abilityType);
        }
        int score = baseScores.getOrDefault(abilityType, BASE_SCORE) + scoreBonuses.getOrDefault(abilityType, 0);
        return Math.clamp(score, MIN_SCORE, MAX_SCORE);
    }

    public void setBaseScore(AbilityTypes abilityType, int modifier) {
        if (modifier == 0f) {
            baseScores.remove(abilityType);
        } else {
            baseScores.put(abilityType, modifier);
        }
    }

    public void addScoreBonus(AbilityTypes abilityType, int modifier) {
        scoreBonuses.merge(abilityType, modifier, Integer::sum);
    }

    public void removeScoreBonus(AbilityTypes abilityType, int modifier) {
        scoreBonuses.merge(abilityType, -modifier, Integer::sum);
    }

    public void resetScoreBonus(AbilityTypes abilityType) {
        scoreBonuses.remove(abilityType);
    }

    public void setOverrideScore(AbilityTypes abilityType, int modifier) {
        if (modifier == 0f) {
            overrideScores.remove(abilityType);
        } else {
            overrideScores.put(abilityType, Math.clamp(modifier, MIN_SCORE, MAX_SCORE));
        }
    }

    public void resetOverrideScore(AbilityTypes abilityType, int modifier) {
        overrideScores.remove(abilityType);
    }

    public void displayScores() {
        for (AbilityTypes abilityType : AbilityTypes.values()) {
            if (overrideScores.containsKey(abilityType)) {
                System.out.printf("%s: {%s}\n",
                    abilityType,
                    overrideScores.get(abilityType)
                );
            } else if (scoreBonuses.containsKey(abilityType)) {
                int modifier = scoreBonuses.get(abilityType);
                String sign = modifier > 0 ? "+" : modifier < 0 ? "-" : "";
                
                System.out.printf("%s: (%s %s %s) %s\n",
                    abilityType,
                    baseScores.getOrDefault(abilityType, BASE_SCORE), 
                    sign,
                    modifier == 0 ? "0" : Math.abs(modifier), 
                    getScore(abilityType)
                );
            } else {
                System.out.printf("%s: %s\n",
                    abilityType,
                    baseScores.getOrDefault(abilityType, BASE_SCORE)
                );
            }
        }
    }
}

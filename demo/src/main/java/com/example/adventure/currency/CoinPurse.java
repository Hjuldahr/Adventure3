package com.example.adventure.currency;

import java.util.EnumMap;

import com.example.adventure.entity.AbilityScores.AbilityCategories;

public class CoinPurse {
    public static final int MAXIMUM = 999;
    public static final int MINIMUM = 0;

    public enum CoinTypes {
        GOLD, SILVER, COPPER
    };

    private EnumMap<CoinTypes,Integer> coins;

    public CoinPurse(int cp, int sp, int gp) {
        coins = new EnumMap<>(CoinTypes.class);
        coins.put(CoinTypes.GOLD, gp);
        coins.put(CoinTypes.SILVER, sp);
        coins.put(CoinTypes.COPPER, cp);
    }

    public CoinPurse(CoinPurse other) {
        coins = other.coins;
    }

    public void setCoins(CoinTypes coinType, int value) {
        coins.put(coinType, constrainValue(value));
    }

    public int getCoins(CoinTypes coinType) {
        return coins.getOrDefault(coinType, 0);
    }

    public void increaseCoins(CoinTypes coinType, int increase) {
        //abilityScores.put(category, value);
        coins.merge(coinType, increase, (value, change) -> 
            constrainValue(value + change)
        );
    }

    public void decreaseCoins(CoinTypes coinType, int decrease) {
        //abilityScores.put(category, value);
        coins.merge(coinType, decrease, (value, change) -> 
            constrainValue(value - change)
        );
    }

    /**
     * Take from purse to another
     */
    public static boolean transfer(CoinPurse from, CoinPurse to, int cp, int sp, int gp) {
        if (from.check(cp, sp, gp)) return false;
        from.take(cp, sp, gp);
        to.give(cp, sp, gp);
        return true;
    }

    public boolean check(int cp, int sp, int gp) {
        return coins.get(CoinTypes.COPPER) >= cp && 
        coins.get(CoinTypes.SILVER) >= sp && 
        coins.get(CoinTypes.GOLD) >= gp;
    }

    public void take(int cp, int sp, int gp) {
        coins.merge(CoinTypes.COPPER, cp, (value, change) -> 
            constrainValue(value - change)
        );
        coins.merge(CoinTypes.SILVER, sp, (value, change) -> 
            constrainValue(value - change)
        );
        coins.merge(CoinTypes.GOLD, gp, (value, change) -> 
            constrainValue(value - change)
        );
    }

    public void give(int cp, int sp, int gp) {
        coins.merge(CoinTypes.COPPER, cp, (value, change) -> 
            constrainValue(value + change)
        );
        coins.merge(CoinTypes.SILVER, sp, (value, change) -> 
            constrainValue(value + change)
        );
        coins.merge(CoinTypes.GOLD, gp, (value, change) -> 
            constrainValue(value + change)
        );
    }

    private int constrainValue(int value) {
        if (value < MINIMUM) {
            return MINIMUM;
        } else if (value > MAXIMUM) {
            return MAXIMUM;
        }
        return value;
    }
}
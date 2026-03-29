package com.example.adventure.randomizer;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Dice {
    public enum RollTypes {
        STANDARD, MINIMUM, AVERAGE, MAXIMUM, KEEP_HIGHEST, KEEP_LOWEST, FIXED
    }

    private final int diceCount;
    private final int diceSides;
    private final int diceModifier;
    private final RollTypes rollType;

    public Dice(int diceSides) { this(1, diceSides, 0, RollTypes.STANDARD); }
    public Dice(int diceCount, int diceSides) { this(diceCount, diceSides, 0, RollTypes.STANDARD); }
    public Dice(int diceCount, int diceSides, int diceModifier) { this(diceCount, diceSides, diceModifier, RollTypes.STANDARD); }

    public Dice(int diceCount, int diceSides, int diceModifier, RollTypes rollType) {
        this.diceCount = diceCount;
        this.diceSides = diceSides;
        this.diceModifier = diceModifier;
        this.rollType = rollType;
    }

    public Dice(Dice other) {
        this(other.diceCount, other.diceSides, other.diceModifier, other.rollType);
    }

    // Getters
    public int getCount() { return diceCount; }
    public int getSides() { return diceSides; }
    public int getModifier() { return diceModifier; }
    public RollTypes getRollTypes() { return rollType; }

    public int roll() { return roll(this.rollType); }

    public int roll(RollTypes override) {
        return switch (override) {
            case FIXED -> diceModifier;
            case STANDARD -> getStandard();
            case MINIMUM -> getMinimum();
            case AVERAGE -> getAverage();
            case MAXIMUM -> getMaximum();
            case KEEP_LOWEST -> getLowest();
            case KEEP_HIGHEST -> getHighest();
        };
    }

    public int getStandard() {
        return rawRolls().sum() + diceModifier;
    }

    public int getLowest() {
        return rawRolls().min().orElse(0) + diceModifier;
    }

    public int getHighest() {
        return rawRolls().max().orElse(0) + diceModifier;
    }

    public int getAverage() {
        return Math.floorDiv(this.diceCount * (this.diceSides + 1), 2) + this.diceModifier;
    }

    public int getMaximum() {
        return (diceCount * diceSides) + diceModifier;
    }

    public int getMinimum() {
        return diceCount + diceModifier;
    }

    private int rawRoll() {
        if (diceSides <= 1) return diceSides;
        return ThreadLocalRandom.current().nextInt(diceSides) + 1;
    }

    public IntStream rawRolls() {
        return IntStream.generate(this::rawRoll).limit(diceCount);
    }

    public static Dice parse(String formula) {
        formula = formula.toLowerCase().trim();
        RollTypes type = RollTypes.STANDARD;

        if (!formula.contains("d")) {
            return new Dice(0, 0, Integer.parseInt(formula), RollTypes.FIXED);
        }

        // Handle Prefixes
        if (formula.startsWith("min ")) { type = RollTypes.MINIMUM; formula = formula.substring(4); }
        else if (formula.startsWith("max ")) { type = RollTypes.MAXIMUM; formula = formula.substring(4); }
        else if (formula.startsWith("avg ")) { type = RollTypes.AVERAGE; formula = formula.substring(4); }
        
        // Handle Suffixes
        if (formula.endsWith(" kh")) { type = RollTypes.KEEP_HIGHEST; formula = formula.substring(0, formula.length() - 3); }
        else if (formula.endsWith(" kl")) { type = RollTypes.KEEP_LOWEST; formula = formula.substring(0, formula.length() - 3); }

        // Removes spaces so "2d10 - 1" becomes "2d10-1"
        formula = formula.replace(" ", "");

        // Changes implicit count to explicit value so d4 becomes 1d4
        if (formula.startsWith("d")) formula = "1" + formula;

        String[] parts = formula.split("d");
        int count = Integer.parseInt(parts[0]);

        // Split sides and modifier (handles +/-)
        String[] subParts = parts[1].split("(?=[+-])");
        int sides = Integer.parseInt(subParts[0]);
        int modifier = (subParts.length > 1) ? Integer.parseInt(subParts[1]) : 0;

        return new Dice(count, sides, modifier, type);
    }

    @Override
    public String toString() {
        if (rollType == RollTypes.FIXED) return String.valueOf(diceModifier);

        StringBuilder sb = new StringBuilder();
        if (rollType == RollTypes.MINIMUM) sb.append("min ");
        if (rollType == RollTypes.MAXIMUM) sb.append("max ");
        if (rollType == RollTypes.AVERAGE) sb.append("avg ");

        if (diceCount > 1) sb.append(diceCount);
        sb.append("d").append(diceSides);

        if (diceModifier != 0) sb.append(diceModifier > 0 ? "+" : "").append(diceModifier);
        if (rollType == RollTypes.KEEP_HIGHEST) sb.append(" kh");
        if (rollType == RollTypes.KEEP_LOWEST) sb.append(" kl");

        return sb.toString();
    }

    public static int D20() {
        return getCurrent().nextInt(20) + 1;
    }

    private static ThreadLocalRandom getCurrent() {
        return ThreadLocalRandom.current();
    }
}

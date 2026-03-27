package com.example.adventure.randomizer;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Dice {
    public enum RollTypes {
        STANDARD, MINIMUM, MAXIMUM, KEEP_HIGHEST, KEEP_LOWEST, FIXED
    }
    
    private final int diceCount;
    private final int diceSides;
    private final int diceModifier;
    private final RollTypes rollType;

    /**
     * Example d4 -> Dice(4)
     * @param diceSides
     */
    public Dice(int diceSides) {
        this(1, diceSides, 0, RollTypes.STANDARD);
    }
    /**
     * Example 2d4 -> Dice(2, 4)
     * @param diceCount
     * @param diceSides
     */
    public Dice(int diceCount, int diceSides) {
        this(diceCount, diceSides, 0, RollTypes.STANDARD);
    }
    /**
     * Example 2d4+1 -> Dice(2, 4, 1)
     * @param diceCount
     * @param diceSides
     * @param diceModifier
     */
    public Dice(int diceCount, int diceSides, int diceModifier) {
        this(diceCount, diceSides, diceModifier, RollTypes.STANDARD);
    }
    /**
     * Example 2d20 kh -> Dice(2, 4, 1, RollTypes.KEEP_HIGHEST)
     * @param diceCount
     * @param diceSides
     * @param diceModifier
     * @param rollType
     */
    public Dice(int diceCount, int diceSides, int diceModifier, RollTypes rollType) {
        this.diceCount = diceCount;
        this.diceSides = diceSides;
        this.diceModifier = diceModifier;
        this.rollType = rollType;
    }

    /**
     * Cloning constructor
     * @param other
     */
    public Dice(Dice other) {
        this(other.diceCount, other.diceSides, other.diceModifier, other.rollType);
    }

    public int getCount() { return this.diceCount; }
    public int getSides() { return this.diceSides; }
    public int getModifier() { return this.diceModifier; }
    public RollTypes getRollTypes() { return this.rollType; }

    /**
     * Rolls dynamically, using default formula modifier
     * @return
     */
    public int roll() {
        return roll(this.rollType);
    }
    /**
     * Rolls dynamically, using overriden formula modifier
     * @param rollType
     * @return
     */
    public int roll(RollTypes rollTypeOverride) {
        return switch (rollTypeOverride) {
            case FIXED -> 0; // modifier only
            case STANDARD -> rawRolls().sum();
            case MINIMUM -> this.diceCount;
            case MAXIMUM -> this.diceCount * this.diceSides;
            case KEEP_LOWEST -> rawRolls().min().orElse(0);
            case KEEP_HIGHEST -> rawRolls().max().orElse(0);
        } + diceModifier;
    }
    /**
     * Rolls multiple times
     * @return
     */
    public IntStream rawRolls() {
        return IntStream.range(0, this.diceCount).map(i -> rawRoll());
    }
    /**
     * Rolls once
     * @return
     */
    public int rawRoll() {
        if (this.diceSides <= 1) return this.diceSides;
        return getCurrent().nextInt(this.diceSides) + 1;
    }
    private static String removePrefix(String prefix, String string) {
        if (string != null && prefix != null && string.startsWith(prefix)) {
            return string.substring(prefix.length());
        }
        return string;
    }
    private static String removeSuffix(String string, String suffix) {
        if (string != null && suffix != null && string.endsWith(suffix)) { //
            return string.substring(0, string.length() - suffix.length()); //
        }
        return string;
    }
    public static Dice parse(String formula) {
        if (!formula.contains("d")) {
            int value = Integer.parseInt(formula);
            return new Dice(0, 0, value, RollTypes.FIXED);
        }
        
        RollTypes rollType = RollTypes.STANDARD;
        
        // min 2d10+1 -> 2+1 -> 3
        if (formula.startsWith("min ")) {
            rollType = RollTypes.MINIMUM;
            formula = removePrefix("min ", formula); //formula.substring(4);
        }
        // max 2d10+1 -> 2*10+1 -> 21
        else if (formula.startsWith("max ")) {
            rollType = RollTypes.MAXIMUM;
            formula = removePrefix("max ", formula);
        }
        // 2d10+1 kh -> max(3,9)+1 -> 10
        else if (formula.endsWith(" kh")) {
            rollType = RollTypes.KEEP_HIGHEST;
            formula = removeSuffix(formula, " kh");
        }
        // 2d10+1 kl -> min(3,9)+1 -> 4
        else if (formula.endsWith(" kl")) {
            rollType = RollTypes.KEEP_LOWEST;
            formula = removeSuffix(formula, " kl");
        }
        // d4 -> 1d4
        if (formula.startsWith("d")) {
            formula = "1" + formula;
        }
        String[] parts = formula.split("d"); 
        int diceCount = Integer.parseInt(parts[0]);

        String[] subParts = parts[1].split("(?=[+-])");
        int diceSides = Integer.parseInt(subParts[0]);
        int diceModifier = subParts.length > 1 ? Integer.parseInt(subParts[1]) : 0;

        return new Dice(diceCount, diceSides, diceModifier, rollType);
    }
    /**
     * Statically rolls 1d20
     * @return
     */
    public static int D20() {
        return getCurrent().nextInt(20) + 1;
    }
    /**
     * Statically rolls 2d20, keep highest
     * @return 
     */
    public static int D20KH() {
        return Math.max(D20(), D20());
    }
    /**
     * Statically rolls 2d20, keep lowest
     * @return 
     */
    public static int D20KL() {
        return Math.min(D20(), D20());
    }

    private static ThreadLocalRandom getCurrent() {
        return ThreadLocalRandom.current();
    }

    @Override
    public String toString() { 
        if (rollType == RollTypes.FIXED) return String.valueOf(diceModifier);

        String prefix = switch (rollType) {
            case MINIMUM -> "min ";
            case MAXIMUM -> "max ";
            default -> "";
        };
        String suffix = switch (rollType) {
            case KEEP_LOWEST -> " kl";
            case KEEP_HIGHEST -> " kh";
            default -> "";
        };
        String mod = this.diceModifier == 0 ? "" : String.format("%+d", this.diceModifier);

        String text = String.format("%s%sd%s%s%s", 
            prefix,
            this.diceCount == 1 ? "" : this.diceCount, 
            this.diceSides,
            mod,
            suffix
        );
        return text;
    }
}
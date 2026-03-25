package com.example.adventure.utility;

import java.util.stream.IntStream;

public class Dice {
    public enum RollTypes {
        STANDARD, 
        ADVANTAGE, 
        DISADVANTAGE
    }
    
    private final int count;
    private final int sides;

    public Dice(int count, int sides) {
        this.count = count;
        this.sides = sides;
    }
    
    public Dice(int sides) {
        this(1, sides);
    }

    public int roll() {
        return IntStream.range(0, count)
            .map(n -> rollOnce())
            .sum();
    }

    public int rollMax() {
        return sides;
    }

    public static int roll(int count, int sides) {
        return IntStream.range(0, count)
            .map(n -> rollOnce(sides))
            .sum();
    }

    public static int rollD20(RollTypes rollType) {
        return switch (rollType) {
            case ADVANTAGE -> Math.max(rollOnce(20), rollOnce(20)); 
            case DISADVANTAGE -> Math.min(rollOnce(20), rollOnce(20));
            case STANDARD -> rollOnce(20);
        };
    }
    public static int rollD20() {
        return rollOnce(20);
    }

    public int rollAdvantage() {
        return Math.max(rollOnce(), rollOnce());
    }

    public int rollDisadvantage() {
        return Math.min(rollOnce(), rollOnce());
    }

    public int rollOnce() {
        if (count == sides) return count; // static value
        return RNG.randomInt(1, sides + 1);
    }

    public static int rollOnce(int sides) {
        return RNG.randomInt(1, sides + 1);
    }

    public int rollDropLowest() {
        return IntStream.generate(this::rollOnce)
            .limit(count)
            .sorted()
            .skip(1) // Skips the smallest value
            .sum();
    }

    @Override
    public String toString() {
        return count + "d" + sides;
    }

    public static Dice d4() {
        return new Dice(4);
    }

    public static Dice d6() {
        return new Dice(6);
    }

    public static Dice d8() {
        return new Dice(8);
    }

    public static Dice d10() {
        return new Dice(10);
    }

    public static Dice d12() {
        return new Dice(12);
    }

    public static Dice d20() {
        return new Dice(20);
    }

    // Constants style for placeholders and edge cases (like unarmed attacks)
    public static Dice d0() {
        return new Dice(0, 0); // overides roll logic
    }

    public static Dice d1() {
        return new Dice(1, 1); // overides roll logic
    }
}
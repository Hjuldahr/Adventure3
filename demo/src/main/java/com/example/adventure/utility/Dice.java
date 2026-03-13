package com.example.adventure.utility;

import java.util.stream.IntStream;

public record Dice(int count, int sides) {
    public int roll() {
        return IntStream.range(0, count)
            .map(_ -> rollOnce())
            .sum();
    }

    public int rollMax() {
        return sides;
    }

    public static int roll(int count, int sides) {
        return IntStream.range(0, count)
            .map(_ -> rollOnce(sides))
            .sum();
    }

    public static int d20() {
        return rollOnce(20);
    }

    public int rollAdvantage() {
        return Math.max(rollOnce(), rollOnce());
    }

    public int rollDisadvantage() {
        return Math.min(rollOnce(), rollOnce());
    }

    public int rollOnce() {
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
}
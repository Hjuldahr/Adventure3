package com.example.adventure.utility;

import java.util.Random;

public abstract class Dice {
    private static final Random rand = new Random();

    public static int dany(int sides) {
        return rand.nextInt(1, sides + 1);
    }
    public static int d4() {
        return rand.nextInt(1, 5);
    }
    public static int d6() {
        return rand.nextInt(1, 7);
    }
    public static int d8() {
        return rand.nextInt(1, 9);
    }
    public static int d10() {
        return rand.nextInt(1, 11);
    }
    public static int d12() {
        return rand.nextInt(1, 13);
    }
    public static int d20() {
        return rand.nextInt(1, 21);
    }
    public static int d100() {
        return rand.nextInt(1, 101);
    }
    public static int advantage() {
        return Math.max(d20(), d20());
    }
    public static int disadvantage() {
        return Math.min(d20(), d20());
    }
}
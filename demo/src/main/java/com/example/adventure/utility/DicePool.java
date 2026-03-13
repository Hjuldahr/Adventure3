package com.example.adventure.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DicePool 
{
    private List<Dice> dicePool;
    private int flatModifier;

    public DicePool(int flatModifier, List<Dice> dicePool) {
        this.dicePool = new ArrayList<>(dicePool);
        this.flatModifier = flatModifier;
    }

    public DicePool(int flatModifier, Dice... dicePool) {
        this(flatModifier, List.of(dicePool));
    }

    public DicePool(int flatModifier, Dice dice) {
        this(flatModifier);
        dicePool.add(dice);
    }

    public DicePool(int flatModifier) {
        this(flatModifier, new ArrayList<>());
    }

    public DicePool() {
        this(0);
    }

    public void addDice(Dice dice) {
        dicePool.add(dice);
    }

    public void removeDice(Dice dice) {
        dicePool.remove(dice);
    }

    public void setDice(List<Dice> dicePool) {
        this.dicePool = new ArrayList<>(dicePool);
    }

    public void setDice(Dice... dicePool) {
        setDice(List.of(dicePool));
    }

    public List<Dice> getDice() {
        return dicePool;
    }

    public void clearDice() {
        this.dicePool.clear();
    }

    public int getModifier() {
        return flatModifier;
    }

    public void setModifier(int flatModifier) {
        this.flatModifier = flatModifier;
    }

    public int rollAll() {
        return dicePool.stream()
            .mapToInt(Dice::roll)
            .sum() + flatModifier;
    }

    public int rollMax() {
        return dicePool.stream()
            .mapToInt(Dice::rollMax)
            .sum() + flatModifier;
    }

    public int[] rollPool() {
        return dicePool.stream()
            .mapToInt(Dice::roll)
            .toArray();
    }

    @Override
    public String toString() {
        String diceString = dicePool.stream()
            .map(Dice::toString)
            .collect(Collectors.joining(" + "));

        if (flatModifier == 0) return diceString.isEmpty() ? "0" : diceString;
        
        // If pool is empty, just return the modifier string
        if (diceString.isEmpty()) return String.valueOf(flatModifier);

        // Otherwise, handle the +/- sign logic
        String op = flatModifier > 0 ? " + " : " - ";
        return diceString + op + Math.abs(flatModifier);
    }
}

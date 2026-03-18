package com.example.adventure.item;

import com.example.adventure.utility.Colours;

public class Item implements Comparable<Item> {
    protected ItemRarities rarity;
    protected String name;
    protected int buyCost;
    protected int sellCost;
    protected int weight;
    protected String verb; // wielding / holding / bearing / etc

    public Item(String name, String verb) {
        this.name = name;
        this.verb = verb;
    }

    public Item(String name) {
        this(name, "holding");
    }

    public String toString() {
        return String.format("%s[%-9s]%s %s\n\tCost: %d/%d\n\tWeight: %d", 
            rarity.getColour(), 
            rarity.getName(), 
            Colours.RESET,
            name, 
            buyCost, sellCost,
            weight
        );
    }

    public String getName() {
        return name;
    }

    public boolean requiresBoth() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'requiresBoth'");
    }

    public boolean canOff() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canOff'");
    }

    public boolean canMain() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canMain'");
    }

    public boolean isVersatile() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isVersatile'");
    }

    public String getVerb() {
        return verb;
    }

    public ItemRarities getRarity() {
        return rarity;
    }

    public int compareTo(Item other) {
        int difference = this.rarity.compareTo(other.rarity);
        if (difference != 0) return difference;
        return this.name.compareToIgnoreCase(other.name);
    }

    public int getBuyCost() {
        return buyCost;
    }
    public int getSellCost() {
        return sellCost;
    }
    public int getWeight() {
        return weight;
    }

    public boolean getIsUnsellable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIsUnsellable'");
    }

    public boolean getIsIndisposable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIsIndisposable'");
    }

    public boolean canAttune() {
        throw new UnsupportedOperationException("Unimplemented method 'canAttune'");
    }

    public boolean isMagical() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isMagical'");
    }
}
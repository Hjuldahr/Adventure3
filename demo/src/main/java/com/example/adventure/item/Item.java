package com.example.adventure.item;

import com.example.adventure.utility.Colours;

public class Item implements Comparable<Item> {
    protected ItemRarities rarity;
    protected String name;
    protected int buyCost;
    protected int sellCost;
    protected int weight;
    protected String verb; // wielding / holding / bearing / etc

    protected Item(String name, String verb) {
        this.name = name;
        this.verb = verb;
    }

    protected Item(String name) {
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

    protected String getName() {
        return name;
    }

    protected String getVerb() {
        return verb;
    }

    protected ItemRarities getRarity() {
        return rarity;
    }

    public int compareTo(Item other) {
        int difference = this.rarity.compareTo(other.rarity);
        if (difference != 0) return difference;
        return this.name.compareToIgnoreCase(other.name);
    }

    protected int getBuyCost() {
        return buyCost;
    }
    protected int getSellCost() {
        return sellCost;
    }
    protected int getWeight() {
        return weight;
    }
    protected boolean getIsUnsellable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIsUnsellable'");
    }

    protected boolean getIsIndisposable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIsIndisposable'");
    }

    protected boolean canAttune() {
        throw new UnsupportedOperationException("Unimplemented method 'canAttune'");
    }

    protected boolean isMagical() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isMagical'");
    }
}
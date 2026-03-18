package com.example.adventure.item;

public class Armour extends Item {

    private int maxAgilityBonus;
    private int armourClass;

    public Armour(String name) {
        super(name);
        //TODO Auto-generated constructor stub
    }

    public int getArmourClass() {
        return armourClass;
    }

    public int getMaxAgilityBonus() {
        return maxAgilityBonus;
    }

    public int getMinBrawnScore() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMinBrawnScore'");
    }

    public int getMagicBonus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMagicBonus'");
    }

    public boolean getAddAgility() {
        return maxAgilityBonus > 0;
    }
}

package com.example.adventure.item;

public class Armour extends Item {
    private ArmourTypes armourType; // base stats
    
    private int maxAgilityBonus;
    private int armourClass;
    private int minBrawnScore;

    public Armour(ArmourTypes armourType) {
        super(armourType.getName());
        this.armourType = armourType;
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

    public boolean getAddAgility() {
        return maxAgilityBonus > 0;
    }
}

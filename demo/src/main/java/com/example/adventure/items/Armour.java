package com.example.adventure.items;

public class Armour extends Equipment {
    private int armourBonus;
    
    public Armour(String name, int buyCost) {
        super(name, buyCost);
    }

    public Armour(Armour other) {
        super(other);
    }

    public Armour setArmourBonus(int armourBonus) {
        this.armourBonus = armourBonus;
        return this;
    }
    public int getArmourBonus() { return armourBonus; }
}
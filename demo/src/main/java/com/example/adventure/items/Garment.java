package com.example.adventure.items;

public class Garment extends Equipment {
    private int defenceBonus;
    
    public Garment(String name, int buyCost, int level) {
        super(name, buyCost, level);
    }

    public Garment(Garment other) {
        super(other);
    }

    public Garment setDefenceBonus(int defenceBonus) {
        this.defenceBonus = defenceBonus;
        return this;
    }
    public int getDefenceBonus() { return defenceBonus; }
}
package com.example.adventure.item;

public enum ArmourTypes {
    PADDED("Padded", 11, 100, 0, true, 8, 5),
    LEATHER("Leather", 11, 100, 0, false, 10, 10),
    STUDDED_LEATHER("Studded Leather", 12, 100, 0, false, 13, 45),

    HIDE("Hide", 12, 2, 0, false, 12, 10),
    CHAIN_SHIRT("Chain Shirt", 13, 2, 0, false, 20, 50),
    SCALE_MAIL("Scale Mail", 14, 2, 0, true, 45, 50),
    BREASTPLATE("Breastplate", 14, 2, 0, false, 20, 400),
    HALFPLATE("Halfplate", 15, 2, 0, true, 40, 750),

    RING_MAIL("Ring Mail", 14, 0, 0, true, 40, 30),
    CHAIN_MAIL("Chain Mail", 16, 0, 13, true, 55, 75),
    SPLINT("Splint", 17, 0, 15, true, 60, 200),
    PLATE("Plate", 18, 0, 15, true, 65, 1500);

    private final String name;
    private final int AC;
    private final int maxAgilityBonus;
    private final int minBrawnScore;
    private final boolean stealthDisadvantage;
    private final int weight;
    private final int buyCost;
    private final int sellCost;

    ArmourTypes(String name, int AC, int maxAgilityBonus, int minBrawnScore, boolean stealthDisadvantage, int weight, int buyCost) {
        this.name = name;
        this.AC = AC;
        this.maxAgilityBonus = maxAgilityBonus;
        this.minBrawnScore = minBrawnScore;
        this.stealthDisadvantage = stealthDisadvantage;
        this.weight = weight;
        this.buyCost = buyCost;
        this.sellCost = buyCost / 2;
    }
    public String getName() {
        return name;
    }
    public int getAC() {
        return AC;
    }
    public int getMaxAgilityBonus() {
        return maxAgilityBonus;
    }
    public int getMinBrawnScore() {
        return minBrawnScore;
    }
    public boolean getImposesStealthDisadvantage() {
        return stealthDisadvantage;
    }
    public int getWeight() {
        return weight;
    }
    public int getBuyCost() {
        return buyCost;
    }
    public int getSellCost() {
        return sellCost;
    }
}

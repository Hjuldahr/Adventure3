package com.example.adventure.items;

public class Equipment extends Item {
    protected final int LEVEL_COST_SCALE = 10;

    protected int level;
    protected int experience;
    protected int masteryCost;
    protected boolean isMastered;
    
    public Equipment(String name, int buyCost, int level) {
        super(name, buyCost);

        this.level = level;
        this.experience = 0;
        this.masteryCost = level * LEVEL_COST_SCALE;
    }
    
    public Equipment(Equipment other) {
        super(other);

        this.level = other.level;
        this.experience = other.experience;
        this.masteryCost = other.masteryCost;
        this.isMastered = other.isMastered;
    }

    /**
     * 
     * @param experience
     * @return amount actually spent
     */
    public int investExperience(int spentExperience) {
        if (isMastered || spentExperience <= 0) {
            return 0;
        }

        int required = masteryCost - experience;

        if (spentExperience >= required) {
            experience = masteryCost;
            isMastered = true;
            return required;
        } else {
            experience += spentExperience;
            return spentExperience;
        }
    }

    public boolean isMastered() {
        return isMastered;
    }

    public int getMasteryCost() {
        return masteryCost;
    }

    public int getExperience() {
        return experience;
    }

    
}
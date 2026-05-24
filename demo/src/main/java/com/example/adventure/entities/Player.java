package com.example.adventure.entities;

public class Player extends Entity {
    private final int LEVEL_COST_SCALE = 110;
    
    private int experience;
    
    public Player(String name, int maxHP) {
        super(name, maxHP);
    }

    public void addExperience(int experience) {
        this.experience += experience;

    }

    public int getLevelThreshold() {
        // current level * 110
        return level * LEVEL_COST_SCALE;
    }

    public void viewSheet() {
        System.out.printf("Name: %s\n\n", this.name);

        System.out.printf("HP: %s\n", this.hitPoints);
        System.out.printf("MP: %s\n\n", this.magicPoints);

        System.out.printf("Lvl: %s\n", this.level);
        System.out.printf("Current Exp: %s\n", this.experience);
        System.out.printf("To Next Level: %s\n\n", getLevelThreshold() - this.experience);

        System.out.printf("Accuracy: %s%%\n", this.accuracy);
        System.out.printf("Evasion: %s%%\n", this.evasion);
        System.out.printf("Phys Def: %s\n", this.defence);
        System.out.printf("Magi Def: %s\n\n", this.magicDefence);

        System.out.printf("Brawn: %s\n", this.brawn);
        System.out.printf("Agility: %s\n", this.agility);
        System.out.printf("Fortitude: %s\n", this.fortitude);
        System.out.printf("Charm: %s\n", this.charm);
        System.out.printf("Intellect: %s\n", this.intellect);
        System.out.printf("Spirit: %s\n", this.spirit);
    }
}

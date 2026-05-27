package com.example.adventure.entities;

public class Player extends Entity {
    private final int LEVEL_COST_SCALE = 110;
    
    private int experience;
    
    public Player(String name, Pronouns pronoun, int maxHP) {
        super(name, pronoun, maxHP);
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

        System.out.printf("Accuracy: %s%%\n", this.accuracy * 100);
        System.out.printf("Evasion: %s%%\n", this.evasion * 100);
        System.out.printf("Armour: %s\n", getArmourScore());

        abilityScores.displayScores();
    }

    @Override
    protected int getArmourScore() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'getArmourScore'");
        return 5;
    }
}

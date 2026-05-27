package com.example.adventure.entities;

public class NPC extends Entity {
    protected int armourScore = 10; // TEMP

    public NPC(String name, Pronouns pronoun, int maxHP) {
        super(name, pronoun, maxHP);
    }

    public NPC(NPC other) {
        super(other);
    }

    @Override
    protected int getArmourScore() {
        return armourScore;
    }
}

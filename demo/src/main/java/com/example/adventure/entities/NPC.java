package com.example.adventure.entities;

import com.example.adventure.combat.CombatContext;

public class NPC extends Entity {
    protected int defenceScore = 10; // TEMP

    public NPC(String name, Pronouns pronoun, int maxHP) {
        super(name, pronoun, maxHP);
    }

    public NPC(NPC other) {
        super(other);
    }

    @Override
    protected int getDefenceBonus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDefenceBonus'");
    }

    @Override
    public void middleOfTurn(CombatContext combatContext) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'middleOfTurn'");
    }
}

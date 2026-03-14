package com.example.adventure.action;

import com.example.adventure.entity.Entity;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.DicePool;

public class HealingSpell extends Spell {

    private DicePool dicePool;

    @Override
    protected void applyToTarget(Entity caster, Entity target) {

        int raw = Dice.d20();

        if (raw == 1) return;

        int heal = dicePool.rollAll() + caster.getSpellCastingModifier();

        if (raw == 20) {
            heal += dicePool.rollMax();
        }

        target.applyHeal(heal);

        applyEffect(target);
    }
}
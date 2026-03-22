package com.example.adventure.action.spells;

import java.util.List;

import com.example.adventure.entity.Entity;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.DicePool;

public class AreaHealingSpell extends Spell {
    private DicePool dicePool;

    public AreaHealingSpell() {

    }

    private int rollHealing() {
        return dicePool.rollAll();
    }

    protected void applyToTarget(Entity target, int healing) {
        int raw = Dice.rollD20();
        int effectiveHeal = calculateHealOverride(raw, healing);

        target.applyHeal(effectiveHeal);

        applyEffect(target);
    }
    private int calculateHealOverride(int raw, int healing) {
        return switch (raw) {
            case 20 -> dicePool.rollMax();
            case 1 -> dicePool.rollMin();
            default -> healing;
        };
    }

    @Override
    protected void resolveTargets(Entity caster, List<Entity> targets) {
        int healing = rollHealing();
        targets.forEach(e -> applyToTarget(e, healing));
    }
}
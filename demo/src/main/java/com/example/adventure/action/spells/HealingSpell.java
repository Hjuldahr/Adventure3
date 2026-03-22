package com.example.adventure.action.spells;

import java.util.List;

import com.example.adventure.entity.Entity;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.DicePool;

public class HealingSpell extends Spell {

    private DicePool dicePool;
    private int multiTargetLimit;

    public HealingSpell() {

    }

    protected void applyToTarget(Entity target, int casterHealingBonus) {
        int raw = Dice.rollD20();
        int healing = rollHealing(raw) + casterHealingBonus;

        target.applyHeal(healing);

        applyEffect(target);
    }

    private int rollHealing(int raw) {
        return switch (raw) {
            case 20 -> dicePool.rollMax();
            case 1 -> dicePool.rollMin();
            default -> dicePool.rollAll();
        };
    }

    @Override
    protected void resolveTargets(Entity caster, List<Entity> targets) {
        int casterHealingBonus = caster.getSpellCastingModifier();

        //guard against too many targets (from NPCs dumping their "to heal list" in priority order)
        if (areaType == AreaType.MULTI_TARGET) {
            targets.subList(0, Math.min(targets.size(), multiTargetLimit))
                .forEach(e -> applyToTarget(e, casterHealingBonus));
        } else {
            applyToTarget(targets.getFirst(), casterHealingBonus);
        }
    }
}
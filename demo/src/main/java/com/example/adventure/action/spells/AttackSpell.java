package com.example.adventure.action.spells;

import java.util.List;

import com.example.adventure.combat.DamageTypes;
import com.example.adventure.entity.Entity;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.DicePool;
import com.example.adventure.utility.RollEvaluator;
import com.example.adventure.utility.SuccessTypes;

public class AttackSpell extends Spell {
    private DicePool dicePool;
    private DamageTypes damageType;
    private int multiTargetLimit;

    public AttackSpell() {

    }

    protected void applyToTarget(Entity target, int casterAttackBonus) {
        int casterRaw = Dice.d20();
        int casterResult = casterRaw + casterAttackBonus;

        int targetArmourClass = target.getArmourClass();

        SuccessTypes casterSuccess = RollEvaluator.evaluate(casterRaw, casterResult, targetArmourClass);
        int damage = rollAttackDamage(casterSuccess); 

        if (damage == 0) return;

        target.applyDamage(damage, damageType);
        if (casterSuccess == SuccessTypes.SUCCESS || casterSuccess == SuccessTypes.CRIT_SUCCESS)
            applyEffect(target);
    }   

    private int rollAttackDamage(SuccessTypes type) {
        return switch (type) {
            case CRIT_SUCCESS -> dicePool.rollMax();
            case SUCCESS -> dicePool.rollAll();
            default -> 0;
        };
    }

    @Override
    protected void resolveTargets(Entity caster, List<Entity> targets) {
        int casterAttackBonus = caster.getSpellCastingModifier() + caster.getProfiencyBonus();

        //guard against too many targets (from NPCs dumping their "to kill list" in priority order)
        if (areaType == AreaType.MULTI_TARGET) {
            targets.subList(0, Math.min(targets.size(), multiTargetLimit))
                .forEach(e -> applyToTarget(e, casterAttackBonus));
        } else {
            applyToTarget(targets.getFirst(), casterAttackBonus);
        }
    }
}
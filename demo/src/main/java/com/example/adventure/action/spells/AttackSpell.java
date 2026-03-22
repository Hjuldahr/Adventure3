package com.example.adventure.action.spells;

import java.util.List;

import com.example.adventure.combat.ConditionTypes;
import com.example.adventure.combat.DamageTypes;
import com.example.adventure.entity.Entity;
import com.example.adventure.entity.behaviours.RoleTypes;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.DicePool;
import com.example.adventure.utility.RollEvaluator;
import com.example.adventure.utility.SuccessTypes;
import com.example.adventure.utility.Dice.RollTypes;

public class AttackSpell extends Spell {
    private DicePool dicePool;
    private DamageTypes damageType;
    private int multiTargetLimit;

    public AttackSpell() {
        
    }

    protected void applyToTarget(Entity caster, Entity target) {
        boolean isAtMeleeDistance = //isInMeleeRange(self, target, false); 
        boolean targetIsProne = target.hasCondition(ConditionTypes.PRONE);
        
        // Logic for Prone
        boolean adv = isAtMeleeDistance && targetIsProne;
        boolean dis = !isAtMeleeDistance && targetIsProne;
        RollTypes rollTypes = RollTypes.STANDARD;
        if (adv && !dis) {
            rollTypes = RollTypes.ADVANTAGE;
        } else if (!adv && dis) {
            rollTypes = RollTypes.DISADVANTAGE;
        }
        
        // Logic for Ranged Penalty
        if (this.isRanged() && caster.isInFrontLine()) {
            dis = true; // Adds a source of disadvantage
        }

        // Dice.d20(adv, dis) handles the cancellation internally
        int casterRaw = Dice.rollD20();
        int casterResult = casterRaw + caster.getSpellCastingModifier() + caster.getProfiencyBonus();

        int targetArmourClass = target.getArmourClass();

        SuccessTypes casterSuccess = RollEvaluator.evaluate(casterRaw, casterResult, targetArmourClass);
        int damage = rollAttackDamage(casterSuccess); 

        if (damage == 0) return;

        target.applyDamage(damage, damageType);
        if (casterSuccess == SuccessTypes.SUCCESS || casterSuccess == SuccessTypes.CRIT_SUCCESS)
            applyEffect(target);
    }   

    private boolean isRanged() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isRanged'");
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
        //guard against too many targets (from NPCs dumping their "to kill list" in priority order)
        if (areaType == AreaType.MULTI_TARGET) {
            targets.subList(0, Math.min(targets.size(), multiTargetLimit))
                .forEach(e -> applyToTarget(caster, e));
        } else {
            applyToTarget(caster, targets.getFirst());
        }
    }
}
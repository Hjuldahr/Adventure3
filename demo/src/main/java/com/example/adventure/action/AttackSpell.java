package com.example.adventure.action;

import com.example.adventure.combat.DamageTypes;
import com.example.adventure.entity.Entity;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.DicePool;
import com.example.adventure.utility.Success;
import com.example.adventure.utility.Success.LuckTypes;
import com.example.adventure.utility.Success.SuccessTypes;

public class AttackSpell extends Spell {

    private DicePool dicePool;
    private DamageTypes damageType;

    // Ray of Frost style effect
    @Override
    protected void applyToTarget(Entity caster, Entity target) {
        int casterRaw = Dice.d20();
        int casterResult = casterRaw + caster.getSpellCastingModifier() + caster.getProfiencyBonus();

        int targetArmourClass = target.getArmourClass();

        SuccessTypes casterSuccess = Success.evaluateSuccess(casterResult, targetArmourClass);
        LuckTypes casterLuck = Success.evaluateLuck(casterRaw);

        if (casterSuccess == SuccessTypes.FAILURE || casterSuccess == SuccessTypes.CRIT_FAILURE) {
            return;
        }

        int damage = rollAttackDamage(casterLuck); 

        // double damage dice on >= AC + 10
        if (casterSuccess == SuccessTypes.CRIT_SUCCESS) {
            damage += rollAttackDamage(casterLuck);
        }

        target.applyDamage(damage, damageType);
        applyEffect(target);
    }   

    private int rollAttackDamage(LuckTypes luck) {
        return switch (luck) {
            case TRIUMPH -> dicePool.rollMax(); // Nat 20
            case DESPAIR -> dicePool.rollMin(); // Nat 1
            default -> dicePool.rollAll();
        };
    }
}
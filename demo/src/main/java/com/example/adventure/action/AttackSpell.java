package com.example.adventure.action;

import com.example.adventure.action.DamageType.DamageTypes;
import com.example.adventure.entity.Entity;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.DicePool;
import com.example.adventure.utility.Success;
import com.example.adventure.utility.Success.LuckTypes;
import com.example.adventure.utility.Success.SuccessTypes;

public class AttackSpell extends Spell {

    private DicePool dicePool;
    private DamageTypes damageType;

    @Override
    protected void applyToTarget(Entity caster, Entity target) {
        int raw = Dice.d20();
        int result = raw + caster.getSpellCastingModifier() + caster.getProfiencyBonus();

        int armourClass = target.getArmourClass();

        SuccessTypes success = Success.evaluateSuccess(result, armourClass);
        LuckTypes luck = Success.evaluateLuck(raw);

        if (success == SuccessTypes.FAILURE || success == SuccessTypes.CRIT_FAILURE) {
            return;
        }

        int damage = rollAttackDamage(luck);

        if (success == SuccessTypes.CRIT_SUCCESS) {
            damage += rollAttackDamage(luck);
        }

        target.applyDamage(damage, damageType);

        applyEffect(target);
    }

    private int rollAttackDamage(LuckTypes luck) {

        return switch (luck) {
            case TRIUMPH -> dicePool.rollMax();
            case FUMBLE -> dicePool.rollMin();
            default -> dicePool.rollAll();
        };
    }
}
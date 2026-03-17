package com.example.adventure.action;

import com.example.adventure.entity.AbilityScores.AbilityCategories;
import com.example.adventure.combat.DamageTypes;
import com.example.adventure.entity.Entity;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.DicePool;
import com.example.adventure.utility.Success;
import com.example.adventure.utility.Success.LuckTypes;
import com.example.adventure.utility.Success.SuccessTypes;

public class HarmSpell extends Spell {

    private DicePool dicePool;
    private DamageTypes damageType;
    private AbilityCategories saveType;

    @Override
    protected void applyToTarget(Entity caster, Entity target) {
        int dc = caster.getSpellSaveDifficulty();
        int roll = Dice.d20();
        int total = roll + target.getAbilityModifier(saveType) + target.getSaveModifier(saveType);
        
        SuccessTypes success = Success.evaluateSuccess(total, dc);
        LuckTypes luck = Success.evaluateLuck(roll);

        // Use your existing modifier logic from AreaDamageSpell!
        float modifier = calculateTargetDamageModifier(success, luck);
        int finalDamage = (int) Math.floor(dicePool.rollAll() * modifier);

        if (finalDamage > 0) {
            target.applyDamage(finalDamage, damageType);
        }

        // Standard D&D 5e/PF2 hybrid: Only apply effect on failure
        if (effect != null && (success == SuccessTypes.FAILURE || success == SuccessTypes.CRIT_FAILURE)) {
            applyEffect(target);
        }
    }

    private float calculateTargetDamageModifier(SuccessTypes success, LuckTypes luck) {
        if (noDamageOnSave) {
            return (luck == LuckTypes.TRIUMPH || success == SuccessTypes.CRIT_SUCCESS || success == SuccessTypes.SUCCESS) ? 0f : 1f;
        }

        return switch (luck) {
            case TRIUMPH -> 0f; // nat 20
            case FUMBLE  -> 2f; // nat 1
            default -> switch (success) {
                case CRIT_SUCCESS -> 0.25f; // >= dc+10
                case SUCCESS      -> 0.5f; // >= dc
                case FAILURE      -> 1f; // < dc
                case CRIT_FAILURE -> 1.5f; // < dc-10
                default           -> 1f;
            };
        };
    }
}
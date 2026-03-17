package com.example.adventure.action;

import com.example.adventure.combat.DamageTypes;
import com.example.adventure.entity.AbilityScores.AbilityCategories;
import com.example.adventure.entity.Entity;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.DicePool;
import com.example.adventure.utility.Success;
import com.example.adventure.utility.Success.LuckTypes;
import com.example.adventure.utility.Success.SuccessTypes;

public class AreaDamageSpell extends Spell {

    private DicePool dicePool;
    private DamageTypes damageType;

    private AbilityCategories saveType;

    @Override
    protected void applyToTarget(Entity caster, Entity target) {
        int dc = caster.getSpellSaveDifficulty();

        int raw = Dice.d20();
        int result = raw + target.getAbilityModifier(saveType)
                + target.getSaveModifier(saveType);

        SuccessTypes success = Success.evaluateSuccess(result, dc);
        LuckTypes luck = Success.evaluateLuck(raw);

        float damageModifier = calculateTargetDamageModifier(success, luck);

        int damage = (int) Math.floor(dicePool.rollAll() * damageModifier);

        target.applyDamage(damage, damageType);

        if (effect != null &&
                (success == SuccessTypes.FAILURE ||
                 success == SuccessTypes.CRIT_FAILURE)) {

            target.applyEffect(effect);
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
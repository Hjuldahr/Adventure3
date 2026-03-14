package com.example.adventure.action;

import com.example.adventure.action.DamageType.DamageTypes;
import com.example.adventure.entity.AbilityScores.AbilityCategories;
import com.example.adventure.entity.Entity;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.DicePool;
import com.example.adventure.utility.Success;

public class HarmSpell extends Spell {

    private DicePool dicePool;
    private DamageTypes damageType;
    private AbilityCategories saveType;

    private boolean effectOnSave;

    @Override
    protected void applyToTarget(Entity caster, Entity target) {

        int damage = dicePool.rollAll();
        target.applyDamage(damage, damageType);

        int raw = Dice.d20();
        int result = raw + target.getAbilityModifier(saveType) +
                target.getSaveModifier(saveType);

        int dc = caster.getSpellSaveDifficulty();

        boolean saved = Success.simpleEvaluateSuccess(result, dc);

        if (effectOnSave && !saved) {
            applyEffect(target);
        }
    }
}
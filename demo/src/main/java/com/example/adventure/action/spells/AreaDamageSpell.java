package com.example.adventure.action.spells;

import java.util.List;

import com.example.adventure.combat.DamageTypes;
import com.example.adventure.entity.Entity;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.DicePool;
import com.example.adventure.utility.SuccessTypes;
import com.example.adventure.utility.RollEvaluator;

public class AreaDamageSpell extends Spell {

    private DicePool dicePool;
    private DamageTypes damageType;

    public AreaDamageSpell() {

    }

    // use before looping over targets to reuse damage result
    public int rollDamage() {
        return dicePool.rollAll();
    }

    protected void applyToTarget(Entity target, int spellSaveDC, int damage) {
        int raw = Dice.d20();
        int result = raw + target.getAbilityModifier(saveType)
                + target.getSaveModifier(saveType);

        SuccessTypes success = RollEvaluator.evaluate(raw, result, spellSaveDC);
        int effectiveDamage = calculateDamageOverride(success, damage);

        target.applyDamage(effectiveDamage, damageType);

        if (success == SuccessTypes.FAILURE || success == SuccessTypes.CRIT_FAILURE)
            applyEffect(target);
    }
    private int calculateDamageOverride(SuccessTypes success, int damage) {
        return switch (success) {
            case CRIT_FAILURE -> dicePool.rollMax();
            case FAILURE -> damage; 
            case SUCCESS -> noDamageOnSave ? 0 : Math.max(1, Math.floorDiv(damage, 2)); 
            case CRIT_SUCCESS -> 0;
        };
    }

    @Override
    protected void resolveTargets(Entity caster, List<Entity> targets) {
        int damage = rollDamage();
        int spellSaveDC = caster.getSpellSaveDifficulty();
        targets.forEach(e -> applyToTarget(e, spellSaveDC, damage));
    }
}
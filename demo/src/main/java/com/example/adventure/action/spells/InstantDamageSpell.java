package com.example.adventure.action.spells;

import java.util.List;

import com.example.adventure.combat.DamageTypes;
import com.example.adventure.entity.Entity;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.DicePool;
import com.example.adventure.utility.RollEvaluator;
import com.example.adventure.utility.SuccessTypes;

public class InstantDamageSpell extends Spell {

    private DicePool dicePool;
    private DamageTypes damageType;

    protected void applyToTarget(Entity target, int spellSaveDC) {
        int damage = dicePool.rollAll();
        target.applyDamage(damage, damageType);

        int raw = Dice.rollD20();
        int result = raw + target.getAbilityModifier(saveType) + target.getSaveModifier(saveType);
        SuccessTypes success = RollEvaluator.evaluate(raw, result, spellSaveDC);

        if (success == SuccessTypes.FAILURE || success == SuccessTypes.CRIT_FAILURE) {
            applyEffect(target);
        }
    }

    @Override
    protected void resolveTargets(Entity caster, List<Entity> targets) {
        int spellSaveDC = caster.getSpellSaveDifficulty();

        //guard against too many targets (from NPCs dumping their "to kill list" in priority order)
        if (areaType == AreaType.MULTI_TARGET) {
            targets.subList(0, Math.min(targets.size(), multiTargetLimit))
                .forEach(e -> applyToTarget(e, spellSaveDC));
        } else {
            applyToTarget(targets.getFirst(), spellSaveDC);
        }
    }
}
package com.example.adventure.creature;

import java.util.EnumMap;

import com.example.adventure.combat.DamageTypeHelper.DamageModifierCategories;
import com.example.adventure.combat.DamageTypes;
import com.example.adventure.randomizer.Dice;

public class PlayerClass {
    private EnumMap<DamageTypes, DamageModifierCategories> damageAdjustments;

    public Dice getHitDiceForLevel(int level) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    public EnumMap<DamageTypes,DamageModifierCategories> getDamageAdjustments() {
        return this.damageAdjustments;
    }
}

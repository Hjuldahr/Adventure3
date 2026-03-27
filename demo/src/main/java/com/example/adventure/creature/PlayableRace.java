package com.example.adventure.creature;

import java.util.EnumMap;
import java.util.EnumSet;

import com.example.adventure.combat.DamageTypeHelper.DamageModifierCategories;
import com.example.adventure.combat.DamageTypes;
import com.example.adventure.randomizer.Dice;

public class PlayableRace {
    private final SizeCategory sizeCategory;
    private final EnumMap<DamageTypes,DamageModifierCategories> damageAdjustments;
    private final EnumSet<VisionTypes> visionTypes;
    private final EnumSet<Langauges> langauges;

    public SizeCategory getSizeCategory() {
        return this.sizeCategory;
    }

    public EnumMap<DamageTypes,DamageModifierCategories> getDamageAdjustments() {
        return this.damageAdjustments;
    }

    public EnumSet<VisionTypes> getVisionTypes() {
        return this.visionTypes;
    }

    public EnumSet<Languages> getLanguages() {
        return this.langauges;
    }
}
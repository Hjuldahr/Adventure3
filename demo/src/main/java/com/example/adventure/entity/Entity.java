package com.example.adventure.entity;

import java.util.EnumMap;
import java.util.Set;

import com.example.adventure.action.DamageType.*;
import com.example.adventure.action.DamageType;
import com.example.adventure.action.Effect;
import com.example.adventure.entity.AbilityScores.AbilityCategories;
import com.example.adventure.utility.Constrained;
import com.example.adventure.utility.Dice;

public abstract class Entity 
{
    public String name;
    public Constrained hitpoints;
    public Constrained magicpoints; //used by spells
    public Constrained actionpoints; //used by manuevers

    private EnumMap<DamageTypes,DamageModifierCategories> damageModifiers;
    
    private int profiencyBonus;
    private Set<AbilityCategories> saveProficiencies;

    private AbilityScores abilityScores;
    private AbilityCategories spellCastingAbilityCategory;

    public Entity(
        String name
    ) {
        this.name = name;

        magicpoints = new Constrained(0, 999);
        actionpoints = new Constrained(0, 999);
    }

    public Entity(Entity other) {
        this(
            other.name
        );
    }

    public void applyEffect(Effect effect) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyEffect'");
    }

    public boolean saveCheck() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveCheck'");
    }

    public int getSpellSaveDifficulty() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSpellSaveDifficulty'");
    }

    public boolean saveCheck(int DC, AbilityCategories saveType) {
        int roll = Dice.d20() + abilityScores.getModifier(saveType) + getSaveModifier(saveType);
        return roll >= DC;
    }

    public int getSpellCastingDC() {
        return 8 + profiencyBonus + abilityScores.getModifier(spellCastingAbilityCategory);
    }

    public void applyDamage(int damage, DamageTypes damageType) {
        float modifier = getDamageModifier(damageType);
        int adjustedDamage = Math.round(damage * modifier);
        if (adjustedDamage > 0) // gaurd against negative damage
            hitpoints.decrease(adjustedDamage);
    }

    public void applyHeal(int heal) {
        hitpoints.increase(heal);
    }

    public float getDamageModifier(DamageTypes damageType) {
        DamageModifierCategories category = damageModifiers.getOrDefault(damageType, DamageModifierCategories.NORMAL);
        return DamageType.lookupDamageModifier(category);
    }

    public int getSaveModifier(AbilityCategories saveType) {
        return saveProficiencies.contains(saveType) ? profiencyBonus : 0;
    }
}
package com.example.adventure.action;

import com.example.adventure.entity.Entity;

import java.util.List;

import com.example.adventure.action.DamageType.DamageTypes;
import com.example.adventure.entity.AbilityScores.AbilityCategories;

public abstract class Spell {
    public static enum ActivationType {
        ACTION,
        BONUS_ACTION,
        FREE_ACTION, // consumes no action
        REACTION // requires an external reaction trigger
    };
    public static enum AreaType {
        SELF,
        SINGLE_TARGET, 
        MULTI_TARGET, //manual selection
        AOE //all targets
    }
    public static enum Category {
        ATTACK, 
        HARM, // spell save instead of hit
        HEALING, 
        SUMMONING, 
        WARDING, 
        BLESSING,
        CURSING
    }

    private Category categoryType;

    private String name;

    private int spellLevel;

    private ActivationType activationType;

    private AreaType areaType;
    private int multiTargetLimit;

    private int duration; // measured in turns. 0 if instant

    private AbilityCategories saveType;

    // effect is always applied on hit or fail
    private Effect effect;

    private boolean halfDamageOnSave; // halve harm damage on passed saves
    private boolean effectOnSave; // only apply affect on failed saves vs everytime

    private int potency;
    private DamageTypes damageType;

    public Spell() {

    }

    public String getName() {
        return name;
    }

    // Assumes enemies vs allies will be correctly picked for heals vs attacks
    public void use(Entity caster, List<Entity> targets) {
        switch (areaType) {
            case SELF: // enforce limit
                applyPerTarget(null, caster);
                break;
            case SINGLE_TARGET: // enforce limit
                applyPerTarget(caster, targets.getFirst());
                break;
            case MULTI_TARGET: // enforce limit
                targets.subList(0, multiTargetLimit).forEach(target -> applyPerTarget(caster, target));
                break;
            case AOE: // everything
                targets.forEach(target -> applyPerTarget(caster, target));
                break;
        }
    }

    // summons are unique as it requires instantiating transient (per encounter) entites
    private void applyPerTarget(Entity caster, Entity target) {
        switch (categoryType) {
            case ATTACK: // spell attack check
                attackTarget(caster, target);
                break;
            case HARM: // spell save
                harmTarget(caster, target);
                break;
            case HEALING:
                healTarget(caster, target);
                break;
            case WARDING:
                break;
            case BLESSING:
                break;
            case CURSING:
                break;
            default:
                break;
        }
    }

    private void attackTarget(Entity caster, Entity target) {

    }

    private void harmTarget(Entity caster, Entity target) {
        int damageModifier = 1;
        
        if (halfDamageOnSave && target.saveCheck(caster.getSpellSaveDifficulty(), saveType)) {
            damageModifier = 2;
        }

        int damage = (potency + caster.getSpellCastModifier()) / damageModifier;
        target.applyDamage(damage, damageType); // internally calculate resistance, immunity, vulnerability

        if (effectOnSave) {
            target.applyEffect(effect);
        }
    }

    private void healTarget(Entity caster, Entity target) {
        int heal = (potency + caster.getSpellCastModifier());
        target.applyHeal(heal);

        // why roll a save against healing?
        target.applyEffect(effect);
    }
}

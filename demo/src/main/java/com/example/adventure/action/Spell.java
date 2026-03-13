package com.example.adventure.action;

import com.example.adventure.entity.Entity;

import java.util.List;

import com.example.adventure.action.DamageType.DamageTypes;
import com.example.adventure.entity.AbilityScores.AbilityCategories;
import com.example.adventure.entity.Entity.RollTypes;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.DicePool;

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

    private DicePool dicePool;
    private DamageTypes damageType;

    private boolean requiresConcentration;

    public Spell() {

    }

    public String getName() {
        return name;
    }

    // Assumes enemies vs allies will be correctly picked for heals vs attacks
    public void use(Entity caster, List<Entity> targets) {
        if (requiresConcentration) {
            // automatically breaks previous spell concentration
            caster.setConcentratedSpell(this);
        }

        switch (areaType) {
            case SELF: // enforce limit
                applyPerTarget(caster, caster);
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
                wardTarget(caster, target);
                break;
            case BLESSING:
                blessTarget(caster, target);
                break;
            case CURSING:
                curseTarget(caster, target);
                break;
            default:
                break;
        }
    }

    private void attackTarget(Entity caster, Entity target) {
        int raw = Dice.d20();
        int attackRoll = raw + caster.getSpellCastingModifier() + caster.getProfiencyBonus();
        
        if (raw == 1 || (raw != 20 && !target.hitCheck(attackRoll))) return;
        
        int damageRoll = dicePool.rollAll(); 

        // instead of directly doubling, increase roll by max damage (feels more impactful)
        if (raw == 20) {
            damageRoll += dicePool.rollMax();
        }

        target.applyDamage(damageRoll, damageType); // internally calculate resistance, immunity, vulnerability

        if (effect != null) {
            target.applyEffect(effect);
        }
    }

    private void harmTarget(Entity caster, Entity target) {
        float damageModifier = 1f;
        
        RollTypes rollType = target.saveCheck(caster.getSpellSaveDifficulty(), saveType);
        
        damageModifier = switch (rollType) {
            case TRIUMPH -> 0.25f;
            case SUCCESS -> 0.5f; 
            case FAILURE -> 1f;
            case FUMBLE -> 1.5f; 
        };

        int damage = (int) Math.floor(dicePool.rollAll() * damageModifier);
        target.applyDamage(damage, damageType); // internally calculate resistance, immunity, vulnerability

        if (effectOnSave && effect != null && (rollType == RollTypes.FAILURE || rollType == RollTypes.FUMBLE)) {
            target.applyEffect(effect);
        }
    }

    private void healTarget(Entity caster, Entity target) {
        int raw = Dice.d20();
        
        if (raw == 1) return;
        
        int healRoll = dicePool.rollAll() + caster.getSpellCastingModifier();

        if (raw == 20) {
            healRoll += dicePool.rollMax();
        }
        
        target.applyHeal(healRoll);

        if (effect != null) {
            target.applyEffect(effect);
        }
    }

    private void wardTarget(Entity caster, Entity target) {
        if (effect != null) {
            target.applyEffect(effect);
        }
    }

    private void blessTarget(Entity caster, Entity target) {
        if (effect != null) {
            target.applyEffect(effect);
        }
    }

    private void curseTarget(Entity caster, Entity target) {
        if (!target.simpleSaveCheck(caster.getSpellSaveDifficulty(), saveType)) return;
        
        if (effect != null) {
            target.applyEffect(effect);
        }
    }

    public void concentrationBroken() {
        if (effect != null && requiresConcentration) {
            effect.removeAllEntities();
        }
    }

    public boolean getRequiresConcentration() {
        return requiresConcentration;
    }

    public Effect getEffect() {
        return effect;
    }
}
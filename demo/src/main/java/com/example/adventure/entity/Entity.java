package com.example.adventure.entity;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.example.adventure.action.DamageType.*;
import com.example.adventure.action.DamageType;
import com.example.adventure.action.Effect;
import com.example.adventure.action.Spell;
import com.example.adventure.entity.AbilityScores.AbilityCategories;
import com.example.adventure.utility.Constrained;
import com.example.adventure.utility.Dice;

public abstract class Entity 
{
    public static enum RollTypes {
        FUMBLE, FAILURE, SUCCESS, TRIUMPH
    }
    
    public String name;
    public Constrained hitpoints;
    public Constrained magicpoints; //used by spells
    public Constrained actionpoints; //used by manuevers

    private EnumMap<DamageTypes,DamageModifierCategories> damageModifiers;
    
    private int profiencyBonus;
    private Set<AbilityCategories> saveProficiencies = new HashSet<>();

    private AbilityScores abilityScores;
    private AbilityCategories spellCastingAbilityCategory;
    private int armourClass;

    private Spell concentratedSpell;

    private Map<String,Effect> appliedEffects = new HashMap<>();

    public Entity(
        String name
    ) {
        this.name = name;

        // TODO derive from ability scores
        hitpoints = new Constrained(0, 999);
        magicpoints = new Constrained(0, 999);
    }

    public Entity(Entity other) {
        this(
            other.name
        );
    }

    public void applyEffect(Effect effect) {
        String effectName = effect.getName();

        if (appliedEffects.containsKey(effectName)) {
            appliedEffects.get(effectName).removeEntity(this);
        }

        effect.addEntity(this);
        appliedEffects.put(effectName, effect);
    }

    public RollTypes saveCheck(int difficultyClass, AbilityCategories saveType) {
        int raw = Dice.d20();
        int result = raw + abilityScores.getModifier(saveType) + getSaveModifier(saveType);

        if (result >= difficultyClass + 10) return RollTypes.TRIUMPH;
        if (result <= difficultyClass - 10) return RollTypes.FUMBLE;
        return result >= difficultyClass ? RollTypes.SUCCESS : RollTypes.FAILURE;
    }

    public boolean simpleSaveCheck(int difficultyClass, AbilityCategories saveType) {
        int raw = Dice.d20();
        int result = raw + abilityScores.getModifier(saveType) + getSaveModifier(saveType);
        return result >= difficultyClass;
    }

    public int getSpellCastingDC() {
        return abilityScores.getModifier(spellCastingAbilityCategory) + profiencyBonus + 8;
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

    public int getSpellCastingModifier() {
        return abilityScores.getModifier(spellCastingAbilityCategory);
    }

    public int getSpellSaveDifficulty() {
        return abilityScores.getModifier(spellCastingAbilityCategory) + profiencyBonus + 8;
    }

    public boolean hitCheck(int attackRoll) {
        return attackRoll >= armourClass;
    }

    public int getProfiencyBonus() {
        return profiencyBonus;
    }

    public Spell getConcentratedSpell() {
        return concentratedSpell;
    }

    public void setConcentratedSpell(Spell spell) {
        // protection against manual setting
        if (!spell.getRequiresConcentration()) return;
        
        if (concentratedSpell != null) {
            concentratedSpell.concentrationBroken();
        }
        concentratedSpell = spell;
    }

    public void unsetConcentratedSpell() {
        setConcentratedSpell(null);
    }

    public void applyDamage(int damage, DamageTypes damageType) {
        float modifier = getDamageModifier(damageType);
        int adjustedDamage = Math.round(damage * modifier);
        
        if (adjustedDamage <= 0) return;

        hitpoints.decrease(adjustedDamage);
        
        if (hitpoints.atMinimum())
            unsetConcentratedSpell();
        else if (concentratedSpell != null)
            concentrationCheck(adjustedDamage, damageType);
    }

    public void concentrationCheck(int damage, DamageTypes damageType) {
        int difficultyClass = Math.max(10, Math.floorDiv(damage, 2));

        AbilityCategories concentrationType = switch(damageType) {
            case RADIANCE -> AbilityCategories.SPIRIT;
            case OBLIVIATING -> AbilityCategories.SPIRIT;
            case PSIONIC -> AbilityCategories.INTELLECT;
            default -> AbilityCategories.FORTITUDE;
        };

        RollTypes rollType = saveCheck(difficultyClass, concentrationType);
        
        if (rollType == RollTypes.FAILURE || rollType == RollTypes.FUMBLE)
            unsetConcentratedSpell();
    }

    public void startOfTurn() {
        //apply effects
    }

    public void endOfTurn() {
        appliedEffects.values().removeIf(effect -> {
            boolean isExpired = effect.decayDuration(this);
            
            if (isExpired) {
                // If this was your active concentration, clear the slot
                if (concentratedSpell != null && concentratedSpell.getEffect() == effect) {
                    unsetConcentratedSpell();
                }
                return true;
            }
            return false;
        });
    }
}
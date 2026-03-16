package com.example.adventure.entity;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.adventure.action.Action;
import com.example.adventure.action.Effect;
import com.example.adventure.action.Spell;
import com.example.adventure.combat.DamageTypeHelper;
import com.example.adventure.combat.DamageTypeHelper.DamageModifierCategories;
import com.example.adventure.combat.DamageTypes;
import com.example.adventure.entity.AbilityScores.AbilityCategories;
import com.example.adventure.utility.Constrained;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.Success;

public abstract class Entity 
{
    public String name;
    public Constrained hitpoints;
    public Constrained spellpoints; //used by spells

    public boolean hasAction = true;
    public boolean hasBonusAction = true;
    public boolean hasReaction = true;

    private EnumMap<DamageTypes,DamageModifierCategories> damageModifiers;
    
    private int profiencyBonus;
    private Set<AbilityCategories> saveProficiencies = new HashSet<>();

    private AbilityScores abilityScores;
    private AbilityCategories spellCastingAbilityCategory;

    private Spell concentratedSpell;

    private Map<String,Effect> appliedEffects = new HashMap<>();

    private AllegianceCategories allegiance;
    private boolean hasSurrendered;

    public Entity(
        String name
    ) {
        this.name = name;

        // TODO derive from ability scores
        hitpoints = new Constrained(0, 999);
        spellpoints = new Constrained(0, 999);
    }

    public Entity(Entity other) {
        this(
            other.name
        );
    }

    public void applyEffect(Effect effect) {
        Effect previousEffect = appliedEffects.put(effect.getName(), effect);

        if (previousEffect != null) {
            previousEffect.removeEntity(this);
        }
    }

    public void removeEffect(String name) {
        Effect previousEffect = appliedEffects.remove(name);

        if (previousEffect != null) {
            previousEffect.removeEntity(this);
        }
    }

    /**
     * save proficiency bonus
     * @param saveType
     * @return 
     */
    public int getSaveModifier(AbilityCategories saveType) {
        return saveProficiencies.contains(saveType) ? profiencyBonus : 0;
    }

    public boolean simpleSaveCheck(int difficultyClass, AbilityCategories saveType) {
        int raw = Dice.d20();
        int result = raw + getSaveModifier(saveType);
        return Success.simpleEvaluateSuccess(difficultyClass, result);
    }

    public int getSpellCastingDC() {
        return abilityScores.getModifier(spellCastingAbilityCategory) + profiencyBonus + 8;
    }

    public void applyHeal(int heal) {
        hitpoints.increase(heal);
    }

    public float getDamageModifier(DamageTypes damageType) {
        DamageModifierCategories category = damageModifiers.getOrDefault(damageType, DamageModifierCategories.NORMAL);
        return DamageTypeHelper.lookupDamageModifier(category);
    }

    public int getSpellCastingModifier() {
        return abilityScores.getModifier(spellCastingAbilityCategory);
    }

    public int getSpellSaveDifficulty() {
        return abilityScores.getModifier(spellCastingAbilityCategory) + profiencyBonus + 8;
    }

    public boolean hitCheck(int attackRoll) {
        return attackRoll >= getArmourClass();
    }

    public int getProfiencyBonus() {
        return profiencyBonus;
    }

    public Spell getConcentratedSpell() {
        return concentratedSpell;
    }

    public void setConcentratedSpell(Spell spell) {
        // protection against manual setting
        if (!spell.requiresConcentration()) return;
        
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
        
        if (!simpleSaveCheck(difficultyClass, concentrationType))
            unsetConcentratedSpell();
    }

    public void startOfTurn() {
        // reset actions
        hasAction = true;
        hasBonusAction = true;
        hasReaction = true;

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

    public void performAction(Action action, List<Entity> targets) {
        boolean usesAction = action.getActionUsage();
        boolean usesBonusAction = action.getBonusActionUsage();

        // If it needs a Bonus Action but you have neither a BA nor a Full Action to sub in
        if (usesBonusAction && !hasBonusAction && !hasAction) return;
        // If it needs a Full Action but you are out
        if (usesAction && !usesBonusAction && !hasAction) return;

        boolean success = action.use(this, targets);

        if (!success) return;

        if (usesBonusAction && hasBonusAction) {
            hasBonusAction = false;
        } else if (usesAction && hasAction) {
            hasAction = false;
        }
    }

    public void performReaction(Action reaction, List<Entity> targets) {
        boolean usesReaction = reaction.getReactionUsage();

        if (usesReaction && !hasReaction) {
            return;
        }

        boolean success = reaction.use(this, targets);
        hasReaction = success ? false : hasReaction;
    }

    public int getAbilityModifier(AbilityCategories category) {
        return abilityScores.getModifier(category);
    }

    public abstract int getArmourClass();

    public void turn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'turn'");
    }

    public abstract boolean isDead();

    public void startOfRound() {

    }

    public void endOfRound() {

    }

    public void startOfEncounter() {

    }

    public void endOfEncounter() {

    }

    public boolean hasMultiInitiative() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHasMultiInitiative'");
    }

    public int getAbilityScore(AbilityCategories category) {
        return abilityScores.getAbilityScore(category);
    }

    public String getName() {
        return name;
    }

    public int getInitiativeModifier() {
        // TODO add other bonuses
        return abilityScores.getModifier(AbilityCategories.AGILITY);
    }

    public boolean hasInitiativeAdvantage() {
        // TODO Auto-generated method stub
        return false;
    }

    public AllegianceCategories getAllegiance() {
        return allegiance;
    }

    // should be automatically deleted on being killed
    public boolean getPersistOnDeath() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isDefeated() {
        if (hasSurrendered) return true; // gave up
        if (hitpoints.atMinimum()) return true; // 0 hitpoints
        return hitpoints.getRatio() <= 0.01f && hasFatalCondition(); // if at under 1% HP and has a disabling effect (functionally on deaths door)
    }

    public boolean hasFatalCondition() {
        return appliedEffects.containsKey("Incapacitated") 
            || appliedEffects.containsKey("Petrified") 
            || appliedEffects.containsKey("Unconscious")
            || appliedEffects.containsKey("Paralyzed") // Can't move/react
            || appliedEffects.containsKey("Stunned");   // Can't move/react
    }
}
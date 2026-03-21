package com.example.adventure.entity;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.adventure.action.Action;
import com.example.adventure.action.Spell;
import com.example.adventure.combat.Condition;
import com.example.adventure.combat.DamageTypeHelper;
import com.example.adventure.combat.DamageTypeHelper.DamageModifierCategories;
import com.example.adventure.combat.DamageTypes;
import com.example.adventure.combat.SpellEffect;
import com.example.adventure.entity.Ability.AbilityTypes;
import com.example.adventure.entity.Skills.SkillTypes;
import com.example.adventure.utility.Constrained;
import com.example.adventure.utility.Dice;
import com.example.adventure.utility.Dice.RollTypes;
import com.example.adventure.utility.Success;
import com.example.adventure.utility.Success.LuckTypes;
import com.example.adventure.utility.Success.SuccessTypes;

public abstract class Entity 
{
    private static final Set<String> FATAL_TYPES = Set.of("Incapacitated", "Petrified", "Unconscious", "Paralyzed", "Stunned");
    
    public String name;
    public Constrained hitpoints;
    public Constrained spellpoints; //used by spells

    public boolean hasAction = true;
    public boolean hasBonusAction = true;
    public boolean hasReaction = true;

    private EnumMap<DamageTypes,DamageModifierCategories> damageModifiers;
    private Proficiencies proficiencies;
    
    private int profiencyBonus;

    protected Ability abilities;
    protected Skills skills;
    private AbilityTypes spellCastingAbilityCategory;

    private SpellEffect concentrationEffect; 

    private Set<Condition> activeConditions;
    private Set<SpellEffect> activeSpellEffects;

    private AllegianceCategories allegiance;
    private boolean hasSurrendered;
    private int initiativeCount = 1;
    private boolean hasInitiativeAdvantage;

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

    /**
     * save proficiency bonus
     * @param saveType
     * @return 
     */
    public int getSpellCastingDC() {
        return abilities.getAbilityModifier(spellCastingAbilityCategory) + profiencyBonus + 8;
    }

    public void applyHeal(int heal) {
        hitpoints.increase(heal);
    }

    public float getDamageModifier(DamageTypes damageType) {
        DamageModifierCategories category = damageModifiers.getOrDefault(damageType, DamageModifierCategories.NORMAL);
        return DamageTypeHelper.lookupDamageModifier(category);
    }

    public int getSpellCastingModifier() {
        return abilities.getAbilityModifier(spellCastingAbilityCategory);
    }

    public int getSpellSaveDifficulty() {
        return abilities.getAbilityModifier(spellCastingAbilityCategory) + profiencyBonus + 8;
    }

    public boolean hitCheck(int attackRoll) {
        return attackRoll >= getArmourClass();
    }

    public int getProfiencyBonus() {
        return profiencyBonus;
    }

    public int getSaveModifier(AbilityTypes saveType) {
        int abilityMod = abilities.getAbilityModifier(saveType);
        int profMod = proficiencies.calculateProficiencyBonus(saveType, profiencyBonus);
        return abilityMod + profMod;
    }

    public int getSkillModifier(SkillTypes skillType) {
        int abilityMod = abilities.getAbilityModifier(skillType.getAbilityType());
        int profMod = proficiencies.calculateProficiencyBonus(skillType, profiencyBonus);
        return abilityMod + profMod;
    }

    public boolean simpleSaveCheck(int difficultyClass, AbilityTypes saveType, RollTypes rollType) {
        int raw = Dice.d20(rollType);
        LuckTypes luck = Success.evaluateLuck(raw);
        
        if (luck == LuckTypes.DESPAIR) return false; 
        if (luck == LuckTypes.TRIUMPH) return true;

        int result = raw + getSaveModifier(saveType);
        SuccessTypes success = Success.evaluateSuccess(result, difficultyClass);
        
        return success == SuccessTypes.CRIT_SUCCESS || success == SuccessTypes.SUCCESS;
    }

    public ResultRecord saveCheck(int difficultyClass, AbilityTypes saveType, RollTypes rollType) {
        int raw = Dice.d20(rollType);
        int result = raw + getSaveModifier(saveType); // includes profiency
        SuccessTypes degree = RollEvaluator.evaluate(raw, result, difficultyClass);
        return new ResultRecord(degree, raw, result, rollType);
    }

    public ResultRecord skillCheck(int difficultyClass, SkillTypes skillType, RollTypes rollType) {
        int raw = Dice.d20(rollType);
        int result = raw + getSkillModifier(skillType); // includes profiency
        SuccessTypes successType = RollEvaluator.evaluate(raw, result, difficultyClass);
        return new ResultRecord(successType, raw, result, rollType);
    }

    private record ResultRecord(SuccessTypes degrees, int raw, int result, RollTypes rollType) {}

    public void setConcentration(SpellEffect effect) {
        breakConcentration();
        this.concentrationEffect = effect;
    }

    public void breakConcentration() {
        if (hasConcentration()) {
            SpellEffect temp = concentrationEffect;
            concentrationEffect = null; 
            temp.dispose(); 
        }
    }

    public void concentrationCheck(int damage, DamageTypes damageType) {
        if (!hasConcentration()) return;
        
        int difficultyClass = Math.max(10, damage / 2);

        AbilityTypes concentrationType = switch(damageType) {
            case REFULGENT -> AbilityTypes.SPIRIT;
            case OBLIVIATING -> AbilityTypes.SPIRIT;
            case PSIONIC -> AbilityTypes.INTELLECT;
            default -> AbilityTypes.FORTITUDE;
        };
        
        if (!simpleSaveCheck(difficultyClass, concentrationType, RollTypes.STANDARD)) {
            breakConcentration();
        }
    }

    public void applyDamage(int damage, DamageTypes damageType) {
        float modifier = getDamageModifier(damageType);
        int adjustedDamage = Math.round(damage * modifier);
        
        if (adjustedDamage <= 0) return;

        hitpoints.decrease(adjustedDamage);
        
        if (hitpoints.atMinimum()) {
            breakConcentration();
        } else if (concentrationEffect != null) {
            concentrationCheck(adjustedDamage, damageType);
        }
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

    public int getAbilityModifier(AbilityTypes category) {
        return abilities.getAbilityModifier(category);
    }

    public abstract int getArmourClass();

    public void turn() {

    }

    public void startOfTurn() {
        // reset actions
        hasAction = true;
        hasBonusAction = true;
        hasReaction = true;

        //activeSpellEffects.forEach(SpellEffect::apply);
    }

    public void endOfTurn() {
        activeSpellEffects.forEach(SpellEffect::decay);
    }

    public void startOfRound() {

    }

    public void endOfRound() {

    }

    public void startOfEncounter() {

    }

    public void endOfEncounter() {

    }

    public int getOnitiativeCount() {
        return initiativeCount;
    }

    public int getAbilityScore(AbilityTypes category) {
        return abilities.getAbilityScore(category);
    }

    public String getName() {
        return name;
    }

    public int getInitiativeModifier() {
        // TODO add other bonuses
        return abilities.getAbilityModifier(AbilityTypes.AGILITY);
    }

    public boolean hasInitiativeAdvantage() {
        return hasInitiativeAdvantage;
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
        return activeConditions.stream()
            .anyMatch(e -> FATAL_TYPES.contains(e.getName()));
    }
    
    public void applyEffect(SpellEffect effect) {
        this.activeSpellEffects.add(effect);

        this.activeConditions.add(effect.condition());
        this.activeConditions.addAll(effect.condition().getDependantConditions());
    }

    public void removeEffect(SpellEffect effect) {
        if (!this.activeSpellEffects.remove(effect)) return; 
        
        effect.dispose();
        this.activeConditions.clear(); 

        for (SpellEffect e : activeSpellEffects) {
            this.activeConditions.add(e.condition());
            this.activeConditions.addAll(e.condition().getDependantConditions());
        }
    }

    public boolean hasConcentration() {
        return concentrationEffect != null;
    }

    public SpellEffect getConcentrationEffect() {
        return concentrationEffect;
    }
}
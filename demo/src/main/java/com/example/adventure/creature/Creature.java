package com.example.adventure.creature;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.example.adventure.action.Action;
import com.example.adventure.combat.AllegianceTypes;
import com.example.adventure.combat.CombatEncounter;
import com.example.adventure.combat.Conditions.ConditionTypes;
import com.example.adventure.combat.DamageTypeHelper;
import com.example.adventure.combat.DamageTypeHelper.DamageModifierCategories;
import com.example.adventure.combat.DamageTypes;
import com.example.adventure.combat.SpellEffect;
import com.example.adventure.creature.Ability.AbilityTypes;
import com.example.adventure.creature.Alignment.Alignments;
import com.example.adventure.creature.behaviours.RoleTypes;
import com.example.adventure.item.WeaponTemplates.WeaponTypes;
import com.example.adventure.randomizer.Dice;
import com.example.adventure.randomizer.Dice.RollTypes;
import com.example.adventure.utility.Constrained;
import com.example.adventure.utility.RollEvaluator;
import com.example.adventure.utility.SuccessTypes;

public abstract class Creature //neither NPC nor PC
{
    protected String name;
    protected SizeCategory sizeCategory;
    protected CreatureType creatureType;
    protected Alignments alignment;
    protected Dice hitDice;
    protected HitPoints hitPoints;
    protected Ability abilities;
    protected Proficiencies<AbilityTypes> saveProficiencies;
    protected Proficiencies<SkillTypes> skillProficiencies;
    protected Proficiencies<WeaponTypes> weaponProficiencies;
    protected AbilityTypes spellCastingAbilityType;
    protected int profiencyBonus;
    protected EnumMap<DamageTypes,DamageModifierCategories> damageAdjustments;
    protected EnumSet<VisionTypes> visionTypes;
    protected EnumSet<Langauges> langauges;

    protected AllegianceTypes allegiance; // friend or foe

    // player uses spell slots, npcs dont
    protected Set<Spell> usableSpells;

    protected boolean hasAction = true;
    protected boolean hasBonusAction = true;
    protected boolean hasReaction = true;
    protected boolean hasManueverAction = true;

    protected Set<ConditionTypes> activeConditions;
    protected Set<SpellEffect> activeSpellEffects;

    protected int totalDamageDealtThisEncounter = 0;
    protected int totalHealingGivenThisEncounter = 0;
    protected CombatEncounter combatContext = null;

    public Creature(
        AllegianceTypes allegiance,

        String name,
        SizeCategory sizeCategory,
        CreatureType creatureType,
        Alignments alignment,
        Dice hitDice,
        Ability abilities,
        Proficiencies<AbilityTypes> saveProficiencies,
        Proficiencies<SkillTypes> skillProficiencies,
        EnumMap<DamageTypes,DamageModifierCategories> damageAdjustments,
        EnumSet<VisionTypes> visionTypes,
        EnumSet<Langauges> langauges
    ) {
        this.allegiance = allegiance;
        
        this.name = name;
        this.sizeCategory = sizeCategory;
        this.creatureType = creatureType;
        this.alignment = alignment;
        this.hitPoints = new HitPoints(hitDice);
        this.abilities = new Ability(abilities);
        this.saveProficiencies = new Proficiencies<>(saveProficiencies);
        this.skillProficiencies = new Proficiencies<>(skillProficiencies);
        this.damageAdjustments = new EnumMap<>(damageAdjustments);
        this.visionTypes = EnumSet.copyOf(visionTypes);
        this.langauges = EnumSet.copyOf(langauges);
    }

    public Creature(Creature other) {
        
    }

    /**
     * save proficiency bonus
     * @param saveType
     * @return 
     */
    public int getSpellCastingDC() {
        return abilities.getAbilityModifier(spellCastingAbilityType) + profiencyBonus + 8;
    }

    public void applyHeal(int heal) {
        hitPoints.increase(heal);
    }

    public float getDamageModifier(DamageTypes damageType) {
        DamageModifierCategories category = damageModifiers.getOrDefault(damageType, DamageModifierCategories.NORMAL);
        return DamageTypeHelper.lookupDamageModifier(category);
    }

    public int getSpellCastingModifier() {
        return abilities.getAbilityModifier(spellCastingAbilityType);
    }

    public int getSpellSaveDifficulty() {
        return abilities.getAbilityModifier(spellCastingAbilityType) + profiencyBonus + 8;
    }

    public boolean hitCheck(int attackRoll) {
        return attackRoll >= getArmourClass();
    }

    public int getFlatProfiencyBonus() {
        return profiencyBonus;
    }

    public int getProfiencyBonus(SkillTypes skillType) {
        return skillProficiencies.getBonusValue(skillType, profiencyBonus);
    }

    public int getProfiencyBonus(AbilityTypes saveType) {
        return saveProficiencies.getBonusValue(saveType, profiencyBonus);
    }

    public int getProfiencyBonus(WeaponTypes weaponType) {
        return weaponProficiencies.getBonusValue(weaponType, profiencyBonus);
    }

    public HitPoints getHitPoints() { return hitPoints; }

    public int getSaveModifier(AbilityTypes saveType) {
        int abilityMod = abilities.getAbilityModifier(saveType);
        int profMod = saveProficiencies.getBonusValue(saveType, profiencyBonus);
        return abilityMod + profMod;
    }

    public int getSkillModifier(SkillTypes skillType) {
        int abilityMod = abilities.getAbilityModifier(skillType.getAbilityType());
        int profMod = skillProficiencies.getBonusValue(skillType, profiencyBonus);
        return abilityMod + profMod;
    }

    public boolean simpleSaveCheck(int difficultyClass, AbilityTypes saveType, RollTypes rollType) {
        int raw = Dice.D20(rollType);
        int result = raw + getSaveModifier(saveType);
        SuccessTypes success = RollEvaluator.evaluate(raw, result, difficultyClass);
        return success == SuccessTypes.CRIT_SUCCESS || success == SuccessTypes.SUCCESS;
    }

    public ResultRecord saveCheck(int difficultyClass, AbilityTypes saveType, RollTypes rollType) {
        int raw = Dice.D20(rollType);
        int result = raw + getSaveModifier(saveType); // includes profiency
        SuccessTypes degree = RollEvaluator.evaluate(raw, result, difficultyClass);
        return new ResultRecord(degree, raw, result, rollType);
    }

    public ResultRecord skillCheck(int difficultyClass, SkillTypes skillType, RollTypes rollType) {
        int raw = Dice.D20(rollType);
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

    public void takeDamage(int damage, DamageTypes damageType, boolean wasCritical, Creature attacker) {
        float modifier = getDamageModifier(damageType);
        int adjustedDamage = Math.round(damage * modifier);
        
        if (adjustedDamage <= 0) return;

        hitPoints.takeDamage(adjustedDamage);
        
        if (hitPoints.atZero()) {
            breakConcentration();
        } else if (concentrationEffect != null) {
            concentrationCheck(adjustedDamage, damageType);
        }
    }

    public void performAction(Action action, List<Creature> targets) {
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

    public void performReaction(Action reaction, List<Creature> targets) {
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
        hasManueverAction = true;

        //activeSpellEffects.forEach(SpellEffect::apply);
    }

    public void endOfTurn() {
        activeSpellEffects.forEach(SpellEffect::decay);
    }

    public void startOfRound() {

    }

    public void endOfRound() {

    }

    public void startOfEncounter(CombatEncounter combatContext) {
        cleanupCombatState();
        this.combatContext = combatContext;
    }

    public void endOfEncounter() {
        cleanupCombatState();
    }

    private void cleanupCombatState() {
        combatContext = null;
        hasSurrendered = false;
        totalDamageDealtThisEncounter = 0;
        totalHealingGivenThisEncounter = 0;
    }

    public int getOnitiativeCount() {
        return initiativeCount;
    }

    public int getAbilityScore(AbilityTypes category) {
        return abilities.getAbilityScore(category);
    }

    public String getName() { return this.name; }

    public int getInitiativeModifier() {
        // TODO add other bonuses
        return abilities.getAbilityModifier(AbilityTypes.AGILITY);
    }

    public AllegianceTypes getAllegiance() { return this.allegiance; }
    public Alignments getAlignment() { return this.alignment; }

    // should be automatically deleted on being killed
    public abstract boolean getPersistOnDeath();

    public boolean isDefeated() {
        if (hasSurrendered) return true; // gave up
        if (hitPoints.atMinimum()) return true; // 0 hitPoints
        return hitPoints.getRatio() <= 0.01f && hasFatalCondition(); // if at under 1% HP and has a disabling effect (functionally on deaths door)
    }

    public boolean hasFatalCondition() {
        return activeConditions.stream()
            .anyMatch(e -> FATAL_TYPES.contains(e.getName()));
    }
    
    public void applyEffect(SpellEffect effect) {
        this.activeSpellEffects.add(effect);

        this.activeConditions.add(effect.getCondition());
        this.activeConditions.addAll(effect.getCondition().getDependantConditions());
    }

    public void removeEffect(SpellEffect effect) {
        if (!this.activeSpellEffects.remove(effect)) return; 
        
        effect.dispose();
        this.activeConditions.clear(); 

        for (SpellEffect e : activeSpellEffects) {
            this.activeConditions.add(e.getCondition());
            this.activeConditions.addAll(e.getCondition().getDependantConditions());
        }
    }

    public boolean hasConcentration() {
        return concentrationEffect != null;
    }

    public SpellEffect getConcentrationEffect() {
        return concentrationEffect;
    }

    public float getAssassinMetric(float damageWeight, float healWeight) {
        return (float)(totalDamageDealtThisEncounter) * damageWeight + (float)(totalHealingGivenThisEncounter) * healWeight;
    }

    public CombatEncounter getCombatContext() {
        return combatContext;
    }

    public abstract boolean isMeleeOnly();
    protected abstract boolean isRangedOnly();
    public abstract boolean hasReachAttacks();

    public boolean isInFrontLine() {
        return combatContext.getPosition(this);
    }
    public boolean isInBackLine() {
        return !combatContext.getPosition(this);
    }
    public void moveToFrontLine() {
        combatContext.setPosition(this, true);
    }
    public void moveToBackLine() {
        combatContext.setPosition(this, false);
    }

    protected abstract void attemptAttack(List<Creature> targets);
    protected abstract boolean attemptHeal(List<Creature> targets);

    public boolean hasCondition(ConditionTypes c) {
        return activeConditions.contains(c);
    }
}
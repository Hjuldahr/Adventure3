package com.example.adventure.creature;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import com.example.adventure.combat.AllegianceTypes;
import com.example.adventure.combat.Conditions.ConditionTypes;
import com.example.adventure.combat.DamageTypeHelper.DamageModifierCategories;
import com.example.adventure.combat.DamageTypes;
import com.example.adventure.creature.Ability.AbilityTypes;
import com.example.adventure.creature.behaviours.AssassinAttackTargeting;
import com.example.adventure.creature.behaviours.BattleHealerHealTargeting;
import com.example.adventure.creature.behaviours.BerzerkerAttackTargeting;
import com.example.adventure.creature.behaviours.HunterAttackTargeting;
import com.example.adventure.creature.behaviours.NoAttackTargeting;
import com.example.adventure.creature.behaviours.NoHealTargeting;
import com.example.adventure.creature.behaviours.RoleTypes;
import com.example.adventure.creature.behaviours.SlayerAttackTargeting;
import com.example.adventure.creature.behaviours.SupportHealerHealTargeting;
import com.example.adventure.creature.behaviours.Targeting;
import com.example.adventure.randomizer.Dice;

public abstract class NonPlayerCreature extends Creature
{
    private int armourClass;
    protected RoleTypes role;
    private Targeting attackTargeting;
    private Targeting healTargeting;

    private HashMap<Creature,Integer> aggroMap = null;
    
    public NonPlayerCreature(
        AllegianceTypes allegiance,
        RoleTypes role,

        String name,
        SizeCategory sizeCategory,
        CreatureType creatureType,
        Alignment alignment,
        Dice hitDice,
        Ability abilities,
        Proficiencies<AbilityTypes> saveProficiencies,
        Proficiencies<SkillTypes> skillProficiencies,
        EnumMap<DamageTypes,DamageModifierCategories> damageAdjustments,
        EnumSet<VisionTypes> visionTypes,
        EnumSet<Langauges> langauges
    ) {
        super(allegiance, name, sizeCategory, creatureType, alignment, hitDice, abilities, saveProficiencies, skillProficiencies, damageAdjustments, visionTypes, langauges);
        
        this.role = role;

        this.attackTargeting = switch(this.role) {
            case SLAYER -> new SlayerAttackTargeting(this);
            case HUNTER -> new HunterAttackTargeting(this);
            case ASSASSIN -> new AssassinAttackTargeting(this);
            case SUPPORT_HEALER -> new NoAttackTargeting(this); 
            default -> new BerzerkerAttackTargeting(this); // berzeker & battle healer
        };
        this.healTargeting = switch(this.role) {
            case BATTLE_HEALER -> new BattleHealerHealTargeting(this);
            case SUPPORT_HEALER -> new SupportHealerHealTargeting(this);
            default -> new NoHealTargeting(this); // any other
        };
    }

    public NonPlayerCreature(NonPlayerCreature other) {
        super(other);
        this.role = other.role;
        this.attackTargeting = other.attackTargeting;
        this.healTargeting = other.healTargeting;
    }

    @Override
    public void takeDamage(int damage, DamageTypes damageType, boolean wasCritical, Creature attacker) {
        super.takeDamage(damage, damageType, wasCritical, attacker);

        aggroMap.merge(attacker, damage, Integer::sum);
    }

    public int getArmourClass() {
        return armourClass;
    }

    public RoleTypes getRoleCategory() {
        return this.role;
    }

    @Override 
    public void turn() {
        super.turn();
        
        if (this.hasCondition(ConditionTypes.PRONE) && this.hasManueverAction) {
            this.activeConditions.remove(ConditionTypes.PRONE);
            this.hasManueverAction = false;
        }

        List<Creature> targets = healTargeting.getRankedTargets();

        if (!targets.isEmpty()) {
            // TODO check healing range if needed later
            if (attemptHeal(targets)) return; // might return false if out of spell slots
        }

        targets = attackTargeting.getRankedTargets();

        if (targets.isEmpty() && hasManueverAction) {
            if (this.isMeleeOnly() && this.isInBackLine()) {
                moveToFrontLine();
            } else if (this.isRangedOnly() && this.isInFrontLine()) {
                moveToBackLine();
            }
            hasManueverAction = false;
            targets = attackTargeting.getRankedTargets();
        }

        if (!targets.isEmpty()) {
            attemptAttack(targets);
        }
    }
}
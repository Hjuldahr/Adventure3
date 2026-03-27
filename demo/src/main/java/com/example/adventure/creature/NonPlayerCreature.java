package com.example.adventure.creature;

import java.util.List;

import com.example.adventure.combat.AllegianceTypes;
import com.example.adventure.combat.ConditionTypes;
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

public abstract class NonPlayerCreature extends Creature
{
    private int armourClass;
    protected RoleTypes role;
    private Targeting attackTargeting;
    private Targeting healTargeting;
    
    public NonPlayerCreature(
        String name,
        RoleTypes role,
        AllegianceTypes allegiance
    ) {
        super(name, allegiance);
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
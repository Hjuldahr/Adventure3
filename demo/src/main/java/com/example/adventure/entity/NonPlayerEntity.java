package com.example.adventure.entity;

import java.util.List;

import com.example.adventure.combat.AllegianceTypes;
import com.example.adventure.combat.ConditionTypes;
import com.example.adventure.entity.behaviours.AssassinAttackTargeting;
import com.example.adventure.entity.behaviours.BattleHealerHealTargeting;
import com.example.adventure.entity.behaviours.BerzerkerAttackTargeting;
import com.example.adventure.entity.behaviours.HunterAttackTargeting;
import com.example.adventure.entity.behaviours.NoAttackTargeting;
import com.example.adventure.entity.behaviours.NoHealTargeting;
import com.example.adventure.entity.behaviours.RoleTypes;
import com.example.adventure.entity.behaviours.SlayerAttackTargeting;
import com.example.adventure.entity.behaviours.SupportHealerHealTargeting;
import com.example.adventure.entity.behaviours.Targeting;

public abstract class NonPlayerEntity extends Entity
{
    private int armourClass;
    protected RoleTypes role;
    private Targeting attackTargeting;
    private Targeting healTargeting;
    
    public NonPlayerEntity(
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

    public NonPlayerEntity(NonPlayerEntity other) {
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

        List<Entity> targets = healTargeting.getRankedTargets();

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
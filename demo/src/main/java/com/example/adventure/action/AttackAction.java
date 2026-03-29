package com.example.adventure.action;

import java.util.Set;

import com.example.adventure.combat.DamageTypes;
import com.example.adventure.creature.Ability.AbilityTypes;
import com.example.adventure.randomizer.Dice;

public abstract class AttackAction extends Action {
    public enum AttackRange {
        MELEE, REACHING, RANGED
    }

    protected final Set<DamagePoolRecord> damagePool;
    protected final AttackRange attackRange;
    protected final int targetLimit;
    protected AbilityTypes abilityType;

    /**
     * Multi target, multi damage, generic attack
     * @param name
     * @param damagePool
     * @param attackRange
     * @param targetLimit
     */
    public AttackAction(String name, Set<DamagePoolRecord> damagePool, AttackRange attackRange, int targetLimit, AbilityTypes abilityType) {
        super(name);
        
        this.damagePool = damagePool;
        this.attackRange = attackRange;
        this.targetLimit = targetLimit;
        this.abilityType = abilityType;
    }

    /**
     * Single target, multi damage type, generic attack
     * @param name
     * @param damagePool
     * @param attackRange
     */
    public AttackAction(String name, Set<DamagePoolRecord> damagePool, AttackRange attackRange, AbilityTypes abilityType) {
        this(name, damagePool, attackRange, 1, abilityType);
    }

    /**
     * Single target, multi damage type, melee attack
     * @param name
     * @param damagePool
     * @param attackRange
     */
    public AttackAction(String name, Set<DamagePoolRecord> damagePool, AbilityTypes abilityType) {
        this(name, damagePool, AttackRange.MELEE, abilityType);
    }

    /**
     * Multi target, multi damage type, melee attack
     * @param name
     * @param damagePool
     * @param attackRange
     */
    public AttackAction(String name, Set<DamagePoolRecord> damagePool, int targetLimit, AbilityTypes abilityType) {
        this(name, damagePool, AttackRange.MELEE, targetLimit, abilityType);
    }

    /**
     * Multi target, one damage type, generic attack
     * @param name
     * @param damageDice
     * @param damageType
     * @param attackRange
     * @param targetLimit
     */
    public AttackAction(String name, Dice damageDice, DamageTypes damageType, AttackRange attackRange, int targetLimit, AbilityTypes abilityType) {
        this(
            name, 
            Set.of(new DamagePoolRecord(damageDice, damageType)), 
            attackRange, 
            targetLimit, 
            abilityType
        );
    }

    /**
     * Single target, one damage type, generic attack
     * @param name
     * @param damageDice
     * @param damageType
     * @param attackRange
     */
    public AttackAction(String name, Dice damageDice, DamageTypes damageType, AttackRange attackRange, AbilityTypes abilityType) {
        this(
            name, 
            Set.of(new DamagePoolRecord(damageDice, damageType)), 
            attackRange, 
            abilityType
        );
    }

    /**
     * Multi target, one damage type, melee attack
     * @param name
     * @param damageDice
     * @param damageType
     * @param targetLimit
     */
    public AttackAction(String name, Dice damageDice, DamageTypes damageType, int targetLimit, AbilityTypes abilityType) {
        this(
            name, 
            damageDice, damageType, 
            AttackRange.MELEE, 
            targetLimit, 
            abilityType
        );
    }

    /**
     * Single target, one damage type, melee attack
     * @param name
     * @param damageDice
     * @param damageType
     */
    public AttackAction(String name, Dice damageDice, DamageTypes damageType, AbilityTypes abilityType) {
        this(
            name, 
            damageDice, damageType, 
            AttackRange.MELEE, 
            abilityType
        );
    }
}

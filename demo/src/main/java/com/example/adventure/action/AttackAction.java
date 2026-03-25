package com.example.adventure.action;

import java.util.Set;

import com.example.adventure.combat.DamageTypes;
import com.example.adventure.utility.Dice;

public abstract class AttackAction extends Action {
    public enum AttackRange {
        MELEE, REACHING, RANGED
    }

    public record DamagePoolRecord(Dice damageDice, DamageTypes damageType) {}

    protected final Set<DamagePoolRecord> damagePool;
    protected final AttackRange attackRange;
    protected final int targetLimit;

    /**
     * Multi target, multi damage, generic attack
     * @param name
     * @param damagePool
     * @param attackRange
     * @param targetLimit
     */
    public AttackAction(String name, Set<DamagePoolRecord> damagePool, AttackRange attackRange, int targetLimit) {
        super(name);
        
        this.damagePool = damagePool;
        this.attackRange = attackRange;
        this.targetLimit = targetLimit;
    }

    /**
     * Single target, multi damage type, generic attack
     * @param name
     * @param damagePool
     * @param attackRange
     */
    public AttackAction(String name, Set<DamagePoolRecord> damagePool, AttackRange attackRange) {
        this(name, damagePool, attackRange, 1);
    }

    /**
     * Single target, multi damage type, melee attack
     * @param name
     * @param damagePool
     * @param attackRange
     */
    public AttackAction(String name, Set<DamagePoolRecord> damagePool) {
        this(name, damagePool, AttackRange.MELEE);
    }

    /**
     * Multi target, multi damage type, melee attack
     * @param name
     * @param damagePool
     * @param attackRange
     */
    public AttackAction(String name, Set<DamagePoolRecord> damagePool, int targetLimit) {
        this(name, damagePool, AttackRange.MELEE, targetLimit);
    }

    /**
     * Multi target, one damage type, generic attack
     * @param name
     * @param damageDice
     * @param damageType
     * @param attackRange
     * @param targetLimit
     */
    public AttackAction(String name, Dice damageDice, DamageTypes damageType, AttackRange attackRange, int targetLimit) {
        this(
            name, 
            Set.of(new DamagePoolRecord(damageDice, damageType)), 
            attackRange, 
            targetLimit
        );
    }

    /**
     * Single target, one damage type, generic attack
     * @param name
     * @param damageDice
     * @param damageType
     * @param attackRange
     */
    public AttackAction(String name, Dice damageDice, DamageTypes damageType, AttackRange attackRange) {
        this(
            name, 
            Set.of(new DamagePoolRecord(damageDice, damageType)), 
            attackRange
        );
    }

    /**
     * Multi target, one damage type, melee attack
     * @param name
     * @param damageDice
     * @param damageType
     * @param targetLimit
     */
    public AttackAction(String name, Dice damageDice, DamageTypes damageType, int targetLimit) {
        this(
            name, 
            damageDice, damageType, 
            AttackRange.MELEE, 
            targetLimit
        );
    }

    /**
     * Single target, one damage type, melee attack
     * @param name
     * @param damageDice
     * @param damageType
     */
    public AttackAction(String name, Dice damageDice, DamageTypes damageType) {
        this(
            name, 
            damageDice, damageType, 
            AttackRange.MELEE
        );
    }
}

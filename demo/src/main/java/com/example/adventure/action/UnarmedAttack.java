package com.example.adventure.action;

import java.util.List;

import com.example.adventure.combat.DamageTypes;
import com.example.adventure.creature.Creature;
import com.example.adventure.creature.Ability.AbilityTypes;
import com.example.adventure.randomizer.Dice;
import com.example.adventure.randomizer.Dice.RollType;

public class UnarmedAttack extends AttackAction {

    protected AbilityTypes abilityType;
    
    public UnarmedAttack() {
        super(
            "Unarmed Strike", 
            Dice.parse("1"), DamageTypes.BLUDGEONING, 
            AttackRange.MELEE
        );
        this.abilityType = AbilityTypes.BRAWN;
    }

    /**
     * For classes and races with non-standard unarmed attacks (tabaxi -> d6 slashing + brawn mod or monk -> d4 bludgeoning + agility mod)
     * @param name
     * @param damageDice
     * @param damageType
     * @param abilityType
     */
    public UnarmedAttack(String name, Dice damageDice, DamageTypes damageType, AbilityTypes abilityType) {
        super(
            name, 
            damageDice, damageType, 
            AttackRange.MELEE
        );
        this.abilityType = abilityType;
    }

    @Override
    public boolean perform(Creature actor, List<Creature> targets) {
        if (targets == null || targets.isEmpty()) return false;
        Creature target = targets.getFirst();

        // 1. Attack Roll
        int rawAttackRoll = Dice.D20();
        int attackMod = actor.getAbilityModifier(abilityType);
        int profBonus = actor.getProfiencyBonus();
        int totalAttack = rawAttackRoll + attackMod + profBonus;

        // Automatic miss on 1, or fail to beat AC (if not a natural 20)
        if (rawAttackRoll == 1 || (rawAttackRoll != 20 && !target.hitCheck(totalAttack))) {
            return false;
        }

        // 2. Damage Roll
        DamagePoolRecord dpr = damagePool.iterator().next();
        Dice damageDice = dpr.damageDice();
        
        int damageResult = (rawAttackRoll == 20 ? damageDice.roll(RollType.MAXIMUM) : damageDice.roll()) + attackMod;

        int finalDamage = Math.max(0, damageResult);

        target.applyDamage(finalDamage, dpr.damageType());
        return true;
    }
}

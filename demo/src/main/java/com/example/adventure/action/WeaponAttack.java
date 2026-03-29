package com.example.adventure.action;

import java.util.List;

import com.example.adventure.combat.DamageTypes;
import com.example.adventure.creature.Ability.AbilityTypes;
import com.example.adventure.item.WeaponItem;
import com.example.adventure.creature.Creature;
import com.example.adventure.randomizer.Dice;

public class WeaponAttack extends AttackAction {
    protected WeaponItem weaponItem;
    
    public WeaponAttack(String name, Dice baseDamage, DamageTypes damageType, AttackRange attackRange, AbilityTypes abilityType) {
        super(name, baseDamage, damageType, attackRange, abilityType);
    }

    @Override
    public boolean perform(Creature actor, List<Creature> targets) {
        if (targets == null || targets.isEmpty()) return false;
        Creature target = targets.getFirst();

        int rawAttackRoll = Dice.D20();
        int attackMod = actor.getAbilityModifier(abilityType);
        int profBonus = actor.getProfiencyBonus(weaponItem.getWeaponType());
        int totalAttack = rawAttackRoll + attackMod + profBonus;

        
    }
}

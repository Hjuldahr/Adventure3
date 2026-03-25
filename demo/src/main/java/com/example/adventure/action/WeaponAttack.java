package com.example.adventure.action;

import java.util.List;

import com.example.adventure.combat.DamageTypes;
import com.example.adventure.entity.Ability.AbilityTypes;
import com.example.adventure.entity.Entity;
import com.example.adventure.utility.Dice;

public class WeaponAttack extends AttackAction {

    protected AbilityTypes abilityType;
    
    public WeaponAttack(String name, Dice damageDice, DamageTypes damageType, AttackRange attackRange) {
        super(name, damageDice, damageType, attackRange);
    }

    @Override
    public boolean perform(Entity actor, List<Entity> targets) {
        
    }

}

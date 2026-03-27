package com.example.adventure.creature;

import com.example.adventure.combat.AllegianceTypes;
import com.example.adventure.creature.behaviours.RoleTypes;

public class EnemyCreature extends NonPlayerCreature
{
    public EnemyCreature(
        String name, RoleTypes role
    ) {
        super(name, role, AllegianceTypes.ENEMY);
    }

    public EnemyCreature(EnemyCreature other) {
        super(other);
    }
}

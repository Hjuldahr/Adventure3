package com.example.adventure.entity;

import com.example.adventure.combat.AllegianceTypes;
import com.example.adventure.entity.behaviours.RoleTypes;

public class EnemyEntity extends NonPlayerEntity
{
    public EnemyEntity(
        String name, RoleTypes role
    ) {
        super(name, role, AllegianceTypes.ENEMY);
    }

    public EnemyEntity(EnemyEntity other) {
        super(other);
    }
}

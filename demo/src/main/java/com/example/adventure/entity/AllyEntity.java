package com.example.adventure.entity;

import com.example.adventure.combat.AllegianceTypes;
import com.example.adventure.entity.behaviours.RoleTypes;

public class AllyEntity extends NonPlayerEntity
{
    public AllyEntity(
        String name, RoleTypes role
    ) {
        super(name, role, AllegianceTypes.PARTY);
    }

    public AllyEntity(AllyEntity other) {
        super(other);
    }
}
package com.example.adventure.creature;

import com.example.adventure.combat.AllegianceTypes;
import com.example.adventure.creature.behaviours.RoleTypes;

public class AllyCreature extends NonPlayerCreature
{
    public AllyCreature(
        String name, RoleTypes role
    ) {
        super(name, role, AllegianceTypes.PARTY);
    }

    public AllyCreature(AllyCreature other) {
        super(other);
    }
}
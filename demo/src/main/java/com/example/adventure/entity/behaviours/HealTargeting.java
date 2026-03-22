package com.example.adventure.entity.behaviours;

import java.util.Set;

import com.example.adventure.combat.AllegianceTypes;
import com.example.adventure.combat.CombatEncounter;
import com.example.adventure.entity.Entity;
import com.example.adventure.entity.NonPlayerEntity;

public abstract class HealTargeting extends Targeting {
    protected HealTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    protected Set<Entity> getValidTargets() {
        CombatEncounter context = self.getCombatContext();

        return (self.getAllegiance() == AllegianceTypes.PARTY) ? context.getParty() : context.getEnemies();
    }
}

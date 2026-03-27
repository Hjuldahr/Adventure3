package com.example.adventure.creature.behaviours;

import java.util.Set;

import com.example.adventure.combat.AllegianceTypes;
import com.example.adventure.combat.CombatEncounter;
import com.example.adventure.creature.Creature;
import com.example.adventure.creature.NonPlayerCreature;

public abstract class HealTargeting extends Targeting {
    protected HealTargeting(NonPlayerCreature self) {
        super(self);
    }

    @Override
    protected Set<Creature> getValidTargets() {
        CombatEncounter context = self.getCombatContext();

        return (self.getAllegiance() == AllegianceTypes.PARTY) ? context.getParty() : context.getEnemies();
    }
}

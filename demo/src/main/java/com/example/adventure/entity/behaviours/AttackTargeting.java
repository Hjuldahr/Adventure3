package com.example.adventure.entity.behaviours;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.adventure.combat.AllegianceTypes;
import com.example.adventure.combat.CombatEncounter;
import com.example.adventure.entity.Entity;

public abstract class AttackTargeting extends Targeting {
    protected AttackTargeting(Entity self) {
        super(self);
    }

    protected List<Entity> rankRandomly() {
        List<Entity> entities = new ArrayList<>(getValidTargets());
        Collections.shuffle(entities);
        return entities;
    }

    @Override
    protected Set<Entity> getValidTargets() {
        CombatEncounter context = self.getCombatContext();
        Set<Entity> potentialTargets = (self.getAllegiance() == AllegianceTypes.ENEMY) 
                                        ? context.getParty() 
                                        : context.getEnemies();

        // 1. If the NPC only has Melee (no reach), they can ONLY see the Front Line.
        if (self.isMeleeOnly() && !self.hasReachAttacks()) {
            Set<Entity> frontLine = potentialTargets.stream()
                .filter(e -> context.isMeleeRange(self, e, self.hasReachAttacks()))
                .collect(Collectors.toSet());

            // If the Front Line is empty, they "Advance" and can target the Back Line.
            return frontLine.isEmpty() ? potentialTargets : frontLine;
        }

        // 2. If they have Ranged/Reach/Spells, the whole party is a valid target.
        return potentialTargets;
    }
}
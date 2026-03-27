package com.example.adventure.creature.behaviours;

import java.util.Comparator;
import java.util.List;

import com.example.adventure.creature.Creature;
import com.example.adventure.creature.NonPlayerCreature;

public class AssassinAttackTargeting extends AttackTargeting {
    public AssassinAttackTargeting(NonPlayerCreature self) {
        super(self);
    }

    @Override
    public List<Creature> getRankedTargets() {
        return validTargetsStream()
            // Assassin metric comparator
            .sorted(Comparator.comparing(e -> e.getAssassinMetric(1.0f, 1.5f)))
            .toList();
    }
}
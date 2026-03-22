package com.example.adventure.entity.behaviours;

import java.util.Comparator;
import java.util.List;

import com.example.adventure.entity.Entity;
import com.example.adventure.entity.NonPlayerEntity;

public class AssassinAttackTargeting extends AttackTargeting {
    public AssassinAttackTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    public List<Entity> getRankedTargets() {
        return validTargetsStream()
            // Assassin metric comparator
            .sorted(Comparator.comparing(e -> e.getAssassinMetric(1.0f, 1.5f)))
            .toList();
    }
}
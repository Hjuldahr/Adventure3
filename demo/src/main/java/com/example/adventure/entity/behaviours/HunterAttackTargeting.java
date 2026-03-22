package com.example.adventure.entity.behaviours;

import java.util.Comparator;
import java.util.List;

import com.example.adventure.entity.Entity;
import com.example.adventure.entity.NonPlayerEntity;

public class HunterAttackTargeting extends AttackTargeting {
    public HunterAttackTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    public List<Entity> getRankedTargets() {
        return validTargetsStream()
                // highest max HP -> lowest current HP -> highest level
                .sorted(Comparator.comparingInt((Entity e) -> -e.getHP().getMaxValue())
                        .thenComparingInt(e -> e.getHP().getValue())
                        .thenComparingInt((Entity e) -> -e.getLevel()))
                .toList();
    }
}
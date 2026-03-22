package com.example.adventure.entity.behaviours;

import java.util.Comparator;
import java.util.List;

import com.example.adventure.entity.Entity;
import com.example.adventure.entity.NonPlayerEntity;

public class SlayerAttackTargeting extends AttackTargeting {
    public SlayerAttackTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    public List<Entity> getRankedTargets() {
        return validTargetsStream()
                // lowest current HP -> lowest max HP -> lowest level
                .sorted(Comparator.comparingInt((Entity e) -> e.getHP().getValue())
                        .thenComparingInt(e -> e.getHP().getMaxValue())
                        .thenComparingInt(Entity::getLevel))
                .toList();
    }
}

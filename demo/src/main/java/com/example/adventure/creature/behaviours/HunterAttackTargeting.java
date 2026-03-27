package com.example.adventure.creature.behaviours;

import java.util.Comparator;
import java.util.List;

import com.example.adventure.creature.Creature;
import com.example.adventure.creature.NonPlayerCreature;

public class HunterAttackTargeting extends AttackTargeting {
    public HunterAttackTargeting(NonPlayerCreature self) {
        super(self);
    }

    @Override
    public List<Creature> getRankedTargets() {
        return validTargetsStream()
                // highest max HP -> lowest current HP -> highest level
                .sorted(Comparator.comparingInt((Creature e) -> -e.getHP().getMaxValue())
                        .thenComparingInt(e -> e.getHP().getValue())
                        .thenComparingInt((Creature e) -> -e.getLevel()))
                .toList();
    }
}
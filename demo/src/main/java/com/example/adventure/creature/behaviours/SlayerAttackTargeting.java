package com.example.adventure.creature.behaviours;

import java.util.Comparator;
import java.util.List;

import com.example.adventure.creature.Creature;
import com.example.adventure.creature.NonPlayerCreature;

public class SlayerAttackTargeting extends AttackTargeting {
    public SlayerAttackTargeting(NonPlayerCreature self) {
        super(self);
    }

    @Override
    public List<Creature> getRankedTargets() {
        return validTargetsStream()
                // lowest current HP -> lowest max HP -> lowest level
                .sorted(Comparator.comparingInt((Creature e) -> e.getHP().getValue())
                        .thenComparingInt(e -> e.getHP().getMaxValue())
                        .thenComparingInt(Creature::getLevel))
                .toList();
    }
}

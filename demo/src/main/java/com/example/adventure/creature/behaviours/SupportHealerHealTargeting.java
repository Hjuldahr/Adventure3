package com.example.adventure.creature.behaviours;

import java.util.Comparator;
import java.util.List;

import com.example.adventure.creature.Creature;
import com.example.adventure.creature.NonPlayerCreature;

public class SupportHealerHealTargeting extends HealTargeting {
    public SupportHealerHealTargeting(NonPlayerCreature self) {
        super(self);
    }

    @Override
    public List<Creature> getRankedTargets() {
        if (self.getHP().getRatio() < 0.15f) {
                return List.of(self);
            }
            return validTargetsStream()
                    .filter(e -> !e.getHP().atMaximum())
                    .sorted(Comparator.comparingDouble(e -> e.getHP().getRatio()))
                    .toList();
    }
}

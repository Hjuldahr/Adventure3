package com.example.adventure.entity.behaviours;

import java.util.Comparator;
import java.util.List;

import com.example.adventure.entity.Entity;
import com.example.adventure.entity.NonPlayerEntity;

public class SupportHealerHealTargeting extends HealTargeting {
    public SupportHealerHealTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    public List<Entity> getRankedTargets() {
        if (self.getHP().getRatio() < 0.15f) {
                return List.of(self);
            }
            return validTargetsStream()
                    .filter(e -> !e.getHP().atMaximum())
                    .sorted(Comparator.comparingDouble(e -> e.getHP().getRatio()))
                    .toList();
    }
}

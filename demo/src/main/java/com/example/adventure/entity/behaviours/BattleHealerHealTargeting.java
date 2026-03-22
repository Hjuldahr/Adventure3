package com.example.adventure.entity.behaviours;

import java.util.Comparator;
import java.util.List;

import com.example.adventure.entity.Entity;
import com.example.adventure.entity.NonPlayerEntity;

public class BattleHealerHealTargeting extends HealTargeting {
    private static final float THRESHOLD = 0.5f;

    public BattleHealerHealTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    public List<Entity> getRankedTargets() {
        if (self.getHitPoints().thresholdCheck(THRESHOLD)) {
            return List.of(self);
        }
        // Prioritize most wounded allies
        return validTargetsStream()
                .filter(e -> e.getHitPoints().thresholdCheck(THRESHOLD))
                .sorted(Comparator.comparingDouble(e -> e.getHitPoints().getRatio()))
                .toList();
    }
}
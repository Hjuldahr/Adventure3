package com.example.adventure.creature.behaviours;

import java.util.Comparator;
import java.util.List;

import com.example.adventure.creature.Creature;
import com.example.adventure.creature.NonPlayerCreature;

public class BattleHealerHealTargeting extends HealTargeting {
    private static final float THRESHOLD = 0.5f;

    public BattleHealerHealTargeting(NonPlayerCreature self) {
        super(self);
    }

    @Override
    public List<Creature> getRankedTargets() {
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
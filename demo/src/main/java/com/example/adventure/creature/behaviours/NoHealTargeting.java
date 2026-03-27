package com.example.adventure.creature.behaviours;

import java.util.List;

import com.example.adventure.creature.Creature;
import com.example.adventure.creature.NonPlayerCreature;

public class NoHealTargeting extends HealTargeting {
    public NoHealTargeting(NonPlayerCreature self) {
        super(self);
    }

    @Override
    public List<Creature> getRankedTargets() {
        return List.of();
    }
}

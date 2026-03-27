package com.example.adventure.creature.behaviours;

import java.util.List;

import com.example.adventure.creature.Creature;
import com.example.adventure.creature.NonPlayerCreature;

public class NoAttackTargeting extends AttackTargeting
{
    public NoAttackTargeting(NonPlayerCreature self) {
        super(self);
    }

    @Override
    public List<Creature> getRankedTargets() {
        return List.of();
    }
}

package com.example.adventure.entity.behaviours;

import java.util.List;

import com.example.adventure.entity.Entity;
import com.example.adventure.entity.NonPlayerEntity;

public class NoAttackTargeting extends AttackTargeting
{
    public NoAttackTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    public List<Entity> getRankedTargets() {
        return List.of();
    }
}

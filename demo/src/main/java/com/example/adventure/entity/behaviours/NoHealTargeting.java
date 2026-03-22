package com.example.adventure.entity.behaviours;

import java.util.List;

import com.example.adventure.entity.Entity;
import com.example.adventure.entity.NonPlayerEntity;

public class NoHealTargeting extends HealTargeting {
    public NoHealTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    public List<Entity> getRankedTargets() {
        return List.of();
    }
}

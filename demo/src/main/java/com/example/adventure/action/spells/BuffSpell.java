package com.example.adventure.action.spells;

import java.util.List;

import com.example.adventure.entity.Entity;

public class BuffSpell extends Spell {
    private int duration; // If your engine tracks turns

    protected void applyToTarget(Entity target) {
        // No d20 roll needed - buffs and debuffs just work
        if (effect != null) {
            target.applyEffect(effect);
        }
    }

    @Override
    protected void resolveTargets(Entity caster, List<Entity> targets) {
        // Multi-target logic (like Bless or Haste)
        if (areaType == AreaType.MULTI_TARGET) {
            targets.subList(0, Math.min(targets.size(), multiTargetLimit))
                .forEach(this::applyToTarget);
        } else {
            applyToTarget(targets.getFirst());
        }
    }
}
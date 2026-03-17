package com.example.adventure.combat;

import com.example.adventure.action.Spell;
import com.example.adventure.entity.Entity;

public class Effect {
    private final Spell origin;
    private final Entity target;
    private final Condition condition;

    private int duration;

    public Effect(Spell origin, Entity target, Condition condition, int duration) {
        this.origin = origin;
        this.target = target;
        this.condition = condition;
        this.duration = duration;
    }

    public Effect(Spell origin, Entity target, Condition condition) {
        this(origin, target, condition, 0);
    }

    public void dispose() {
        target.removeEffect(this);
        origin.notifyEffectEnded(this);
    }

    public void decay() {
        duration--;
        if (duration <= 0) {
            dispose();
        }
    }

    public Condition condition() {
        return condition;
    }

    public Spell origin() {
        return origin;
    }

    public Entity target() {
        return target;
    }
}
package com.example.adventure.effect;

import java.util.HashSet;

import com.example.adventure.categories.DamageTypes;

public class ActiveEffects {
    private HashSet<Effect> effects;
    
    public ActiveEffects() {
        effects = new HashSet<>();
    }

    public ActiveEffects(ActiveEffects activeEffects) {
        effects = new HashSet<>(activeEffects.effects);
    }

    public boolean apply(Effect effect) {
        return effects.add(effect);
    }

    public boolean remove(Effect effect) {
        return effects.remove(effect);
    }
}

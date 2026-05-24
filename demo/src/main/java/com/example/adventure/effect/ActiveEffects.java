package com.example.adventure.effect;

import java.util.HashMap;
import java.util.List;

public class ActiveEffects {
    private HashMap<String,Effect> effects;
    
    public ActiveEffects() {
        effects = new HashMap<>();
    }

    public ActiveEffects(ActiveEffects activeEffects) {
        effects = new HashMap<>(activeEffects.effects);
    }

    public void decrementDuration() {
        effects.values().forEach(Effect::decrementDuration);
    }

    public List<Effect> getExpiredEffects() {
        return effects.values().stream()
            .filter(Effect::isExpired)
            .toList();
    }

    public boolean remove(Effect effect) {
        return effects.remove(effect.getName()) != null;
    }

    public boolean add(Effect effect) {
        boolean alreadyPresent = effects.containsKey(effect.getName());
        effects.merge(effect.getName(), effect, Effect::merge);
        return !alreadyPresent;
    }

    public List<Effect> getEffects() {
        return List.copyOf(effects.values());
    }
}
package com.example.adventure.effect;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActiveEffects {
    private Set<Effect> effects;
    
    public ActiveEffects() {
        effects = new HashSet<>();
    }

    public ActiveEffects(ActiveEffects activeEffects) {
        effects = new HashSet<>(activeEffects.effects);
    }

    public void decrementDuration() {
        effects.forEach(Effect::decrementDuration);
    }

    public List<Effect> getExpiredEffects() {
        return effects.stream().filter(Effect::isExpired).toList();
    }

    public boolean remove(Effect effect) {
        return effects.remove(effect);
    }

    public boolean add(Effect effect) {
        return effects.add(effect); // Assuming a Set configuration underneath
    }

    public List<Effect> getEffects() {
        return List.copyOf(effects);
    }
}

package com.example.adventure.action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.adventure.entity.Entity;
import com.example.adventure.utility.Timer;

public class Effect {
    private String name;
    private Set<Entity> entities = new HashSet<>();
    private Map<Entity,Timer> clocks = new HashMap<>();
    
    public void addEntity(Entity target, int duration) {
        this.entities.add(target);
        this.clocks.put(target, new Timer(duration));
    }

    public String getName() {
        return name;
    }

    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    public void removeAllEntities() {
        for (Entity e : entities) {
            e.removeEffectFromMap(this.name); // You'll need this method in Entity
        }
        this.entities.clear();
        this.clocks.clear();
    }

    public boolean decayDuration(Entity entity) {
        boolean ended = clocks.get(entity).tickBound();
        if (ended)
            entities.remove(entity);
        return ended;
    }
}

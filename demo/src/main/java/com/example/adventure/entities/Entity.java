package com.example.adventure.entities;

import com.example.adventure.events.EventListener;
import com.example.adventure.events.EventTypes;

public abstract class Entity {
    protected String name;
    protected EventListener eventListener = new EventListener();
    protected HitPoints hitPoints;

    public Entity(String name, int maxHP) {
        this.name = name;
        this.hitPoints = new HitPoints(maxHP);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public EventListener getEventListener() { return eventListener; }

    public void takeDamage(Entity attacker, int damage) {
        hitPoints.change(-damage);
        eventListener.get(EventTypes.TAKE_DAMAGE).accept(attacker, damage);
    }
}

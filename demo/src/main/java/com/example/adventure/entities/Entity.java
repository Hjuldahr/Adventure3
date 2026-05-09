package com.example.adventure.entities;

import com.example.adventure.events.EventListener;
import com.example.adventure.events.EventTypes;

public abstract class Entity {
    protected String name;
    protected HitPoints hitPoints;

    protected EventListener<PlayerEvents> eventListener;

    public Entity(String name, int maxHP) {
        this.name = name;
        this.hitPoints = new HitPoints(maxHP);

        this.eventListener = new EventListener<>(PlayerEvents.class);
    }

    public Entity(Entity other) {
        this.name = other.name;
        this.hitPoints = new HitPoints(other.hitPoints);

        this.eventListener = new EventListener<>(other.eventListener);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public EventListener<PlayerEvents> getEventListener() { return eventListener; }

    public void receiveHeal(Record params) {
        hitPoints.change(params.heal());
        eventListener.fire(PlayerEvents.TAKE_HEAL, params);
    }

    public void receiveDamage(Record params) {
        hitPoints.change(-params.damage());
        eventListener.fire(PlayerEvents.TAKE_DAMAGE, params);
        if (params.isSpell()) {
            eventListener.fire(PlayerEvents.TAKE_SPELL_DAMAGE, params);
        } else if (params.isWeapon()) {
            eventListener.fire(PlayerEvents.TAKE_WEAPON_DAMAGE, params);
        }
    }
}

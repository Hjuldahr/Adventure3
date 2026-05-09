package com.example.adventure.entities;

import com.example.adventure.context.DataRecord;
import com.example.adventure.context.Keys;
import com.example.adventure.events.EventListener;
import com.example.adventure.events.EventTypes;

public class Entity {
    protected String name;
    protected HitPoints hitPoints;

    protected DamageResistances damageResistances;
    protected float healModifier = 1f;

    protected EventListener eventListener;

    public Entity(String name, int maxHP) {
        this.name = name;
        this.hitPoints = new HitPoints(maxHP);

        this.eventListener = new EventListener();
        this.damageResistances = new DamageResistances();
    }

    public Entity(Entity other) {
        this.name = other.name;
        this.hitPoints = new HitPoints(other.hitPoints);

        this.eventListener = new EventListener(other.eventListener);
        this.damageResistances = new DamageResistances(other.damageResistances);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public EventListener getEventListener() { return eventListener; }

    public void receiveHeal(DataRecord params) {
        int baseHealing = params.get(Keys.HEAL);
        // -50% = harmed by healing, 0% = cannot be healed, 100% = base, 150% = bonus
        int finalHeal = (int) (baseHealing * healModifier);

        params.set(Keys.EFFECTIVE_HEAL, finalHeal);
        eventListener.fire(EventTypes.TAKE_HEAL, params);

        hitPoints.change(finalHeal);
    }

    public void receiveDamage(DataRecord params) {
        int baseDamage = params.get(Keys.DAMAGE);
        DamageTypes damageType = params.get(Keys.DAMAGE_TYPE);

        float damageModifier = damageResistances.getResistanceModifier(damageType);
        int finalDamage = Math.clamp(Math.round(baseDamage * damageModifier), 0, 9_999);

        params.set(Keys.EFFECTIVE_DAMAGE, finalDamage);
        eventListener.fire(EventTypes.TAKE_DAMAGE, params);

        hitPoints.change(-finalDamage);
    }

    public void performWeaponAttack(DataRecord params) {
        eventListener.fire(EventTypes.DID_DAMAGE, params);
        eventListener.fire(EventTypes.DID_WEAPON_ATTACK, params);

        params.get(Keys.TARGET).receiveDamage(params);
    }

    public void performSpellAttack(DataRecord params) {
        eventListener.fire(EventTypes.DID_DAMAGE, params);
        eventListener.fire(EventTypes.DID_SPELL_ATTACK, params);

        params.get(Keys.TARGET).receiveDamage(params);
    }

    public void performHeal(DataRecord params) {
        eventListener.fire(EventTypes.DID_HEAL, params);

        params.get(Keys.TARGET).receiveHeal(params);
    }
}

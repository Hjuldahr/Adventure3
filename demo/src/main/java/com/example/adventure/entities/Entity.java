package com.example.adventure.entities;

import com.example.adventure.categories.AbilityScores;
import com.example.adventure.categories.DamageModifiers;
import com.example.adventure.categories.DamageTypes;
import com.example.adventure.context.DataRecord;
import com.example.adventure.context.Keys;
import com.example.adventure.effect.ActiveEffects;
import com.example.adventure.effect.Effect;
import com.example.adventure.events.EventListener;
import com.example.adventure.events.EventTypes;

public class Entity {
    protected String name;
    protected int level = 1;

    protected Resource hitPoints;
    protected Resource magicPoints;

    protected DamageModifiers damageModifiers;
    protected AbilityScores abilityScores;
    protected ActiveEffects activeEffects;
    protected float healModifier = 1f;

    protected EventListener eventListener;

    protected int speed;

    public Entity(String name, int maxHP) {
        this.name = name;

        this.hitPoints = new Resource(maxHP);
        this.eventListener = new EventListener();
        this.activeEffects = new ActiveEffects();
        this.damageModifiers = new DamageModifiers();
        this.abilityScores = new AbilityScores();
    }

    public Entity(Entity other) {
        this.name = other.name;

        this.hitPoints = new Resource(other.hitPoints);
        this.eventListener = new EventListener(other.eventListener);
        this.activeEffects = new ActiveEffects(other.activeEffects);
        this.damageModifiers = new DamageModifiers(other.damageModifiers);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getSpeed() { return speed; }

    public EventListener getEventListener() { return eventListener; }

    public void receiveHeal(DataRecord params) {
        int baseHealing = params.get(Keys.APPLY_DAMAGE);
        // -50% = harmed by healing, 0% = cannot be healed, 100% = base, 150% = bonus
        int finalHeal = (int) (baseHealing * healModifier);

        eventListener.fire(EventTypes.TAKE_HEAL, params);
        hitPoints.change(finalHeal);
    }

    public void receiveDamage(DataRecord params) {
        int baseDamage = params.get(Keys.APPLY_DAMAGE);
        DamageTypes damageType = params.get(Keys.DAMAGE_TYPE);

        float damageModifier = damageModifiers.getModifier(damageType);
        int finalDamage = Math.clamp(Math.round(baseDamage * damageModifier), 0, 9_999);

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

    public void middleOfTurn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'act'");
    }

    public void startOfTurn() {
        for (Effect effect : activeEffects.getEffects()) {
            DataRecord params = effect.getParams();
            
            if (params.has(Keys.APPLY_HEAL)) {
                receiveHeal(params);
            }
        }
    }

    public void applyEffect(Effect effect) {
        if (!activeEffects.add(effect)) return; 
        
        System.out.printf("[+] %s\n", effect.getName());

        DataRecord params = effect.getParams();
        
        if (params.has(Keys.RECEIVED_DAMAGE_MODIFIER)) {
            damageModifiers.addTemporaryModifier(
                params.get(Keys.DAMAGE_TYPE), 
                params.get(Keys.RECEIVED_DAMAGE_MODIFIER)
            );
        }

        if (params.has(Keys.APPLY_HEAL)) {
            receiveHeal(params);
        }
    }

    public void endOfTurn() {
        for (Effect effect : activeEffects.getEffects()) {
            DataRecord params = effect.getParams();
            
            if (params.has(Keys.APPLY_DAMAGE)) {
                receiveDamage(params);
            }
        }

        activeEffects.decrementDuration();
        activeEffects.getExpiredEffects().forEach(this::removeEffect);
    }

    public void removeEffect(Effect effect) {
        if (!activeEffects.remove(effect)) return;

        System.out.printf("[-] %s\n", effect.getName());

        DataRecord params = effect.getParams();

        if (params.has(Keys.RECEIVED_DAMAGE_MODIFIER)) {
            damageModifiers.removeTemporaryModifier(
                params.get(Keys.DAMAGE_TYPE), 
                params.get(Keys.RECEIVED_DAMAGE_MODIFIER)
            );
        }
    }
}

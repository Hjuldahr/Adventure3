package com.example.adventure.entities;

import java.util.concurrent.ThreadLocalRandom;

import com.example.adventure.categories.AbilityScores;
import com.example.adventure.categories.DamageModifiers;
import com.example.adventure.categories.DamageTypes;
import com.example.adventure.context.DataRecord;
import com.example.adventure.context.Keys;
import com.example.adventure.effect.ActiveEffects;
import com.example.adventure.effect.Effect;
import com.example.adventure.events.EventListener;
import com.example.adventure.events.EventTypes;

public abstract class Entity {
    protected final float CRIT_RATE = 0.05f;
    protected final float CRIT_BONUS = 1.5f;
    
    protected String name;
    protected int level = 1;

    protected Resource hitPoints;
    protected Resource magicPoints;

    protected DamageModifiers attackModifiers;
    protected DamageModifiers defenceModifiers;

    protected AbilityScores abilityScores;
    protected ActiveEffects activeEffects;
    protected float healModifier = 1f;
    protected float accuracy = 0.5f;
    protected int speed = 1;
    protected float evasion = 0.5f;
    protected int magicPointRegeneration = 1;

    protected EventListener eventListener;

    public Entity(String name, int maxHP) {
        this.name = name;

        this.hitPoints = new Resource(maxHP);
        this.eventListener = new EventListener();
        this.activeEffects = new ActiveEffects();
        this.attackModifiers = new DamageModifiers();
        this.defenceModifiers = new DamageModifiers();
        this.abilityScores = new AbilityScores();
    }

    public Entity(Entity other) {
        this.name = other.name;

        this.hitPoints = new Resource(other.hitPoints);
        this.eventListener = new EventListener(other.eventListener);
        this.activeEffects = new ActiveEffects(other.activeEffects);
        this.attackModifiers = new DamageModifiers(other.attackModifiers);
        this.defenceModifiers = new DamageModifiers(other.defenceModifiers);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getSpeed() { return speed; }

    public EventListener getEventListener() { return eventListener; }

    public void receiveHeal(DataRecord params) {
        int baseHealing = params.get(Keys.HEALING_TAKEN);
        int finalHeal = (int) (baseHealing * healModifier);

        eventListener.fire(EventTypes.TAKE_HEAL, params);
        hitPoints.change(finalHeal);
    }

    private float getArmourModifier() {
        return 100f / (100f + getArmourScore());
    }

    protected abstract int getArmourScore();

    private float getEvasion() {
        return evasion;
    }

    public void performWeaponAttack(DataRecord params) {
        Entity target = params.get(Keys.TARGET);

        float evasion = target.getEvasion();
        float denominator = accuracy + evasion;
        float hitThreshold = (denominator <= 0) ? 0.5f : (accuracy / denominator);

        if (ThreadLocalRandom.current().nextFloat() <= hitThreshold) {
            int damage = params.get(Keys.ATTACK_POWER);
            
            if (ThreadLocalRandom.current().nextFloat() <= CRIT_RATE) {
                damage = (int) Math.ceil(damage * CRIT_BONUS);
                params.set(Keys.IS_CRITICAL_HIT, null);
                System.out.println("Critical hit!");
            } else {
                System.out.println("Hit!");
            }
            
            params.set(Keys.DAMAGE_TAKEN, damage);
            target.receiveWeaponDamage(params);
        } else {
            System.out.println("Miss!");
        }
    }

    public void performSpellAttack(DataRecord params) {
        Entity target = params.get(Keys.TARGET);
        DamageTypes damageType = params.get(Keys.DAMAGE_TYPE);
        
        int damage = params.get(Keys.ATTACK_POWER);
        damage = (int) Math.ceil(damage * attackModifiers.getModifier(damageType));
        
        if (ThreadLocalRandom.current().nextFloat() <= CRIT_RATE) {
            damage = (int) Math.ceil(damage * CRIT_BONUS);
            System.out.println("Critical hit!");
        } else {
            System.out.println("Hit!");
        }
        
        params.set(Keys.DAMAGE_TAKEN, damage);
        target.receiveSpellDamage(params);
    }

    protected void receiveWeaponDamage(DataRecord params) {
        float armorModifier = getArmourModifier();
        float typeModifier = defenceModifiers.getModifier(DamageTypes.PHYSICAL);
        
        applyDamage(params, armorModifier * typeModifier);
    }

    protected void receiveSpellDamage(DataRecord params) {
        DamageTypes damageType = params.get(Keys.DAMAGE_TYPE);

        float armorModifier = damageType.ignoresArmour() ? 1f : getArmourModifier();
        float typeModifier = defenceModifiers.getModifier(damageType);
        
        applyDamage(params, armorModifier * typeModifier);
    }

    /**
     * Single source of truth for changing health pools and publishing events.
     */
    private void applyDamage(DataRecord params, float finalModifier) {
        int baseDamage = params.get(Keys.DAMAGE_TAKEN);
        
        int finalDamage = Math.clamp(Math.round(baseDamage * finalModifier), 1, 9_999);
        params.set(Keys.DAMAGE_TAKEN, finalDamage);

        System.out.printf("%s has taken %s %s damage!\n", name, finalDamage, params.get(Keys.DAMAGE_TYPE));

        eventListener.fire(EventTypes.TAKE_DAMAGE, params);
        hitPoints.change(-finalDamage);
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
        magicPoints.change(magicPointRegeneration);

        for (Effect effect : activeEffects.getEffects()) {
            DataRecord params = effect.getParams();
            
            if (params.has(Keys.HEALING_OVER_TIME)) {
                receiveHeal(params);
            }
        }
    }

    public void applyEffect(Effect effect) {
        if (!activeEffects.add(effect)) return; 
        
        System.out.printf("[+] %s\n", effect.getName());

        DataRecord params = effect.getParams();
        
        //if (params.has(Keys.RECEIVED_DAMAGE_MODIFIER)) {
        //    damageModifiers.addTemporaryModifier(
        //        params.get(Keys.DAMAGE_TYPE), 
        //        params.get(Keys.RECEIVED_DAMAGE_MODIFIER)
        //    );
        //}

        if (params.has(Keys.HEALING_OVER_TIME)) {
            receiveHeal(params);
        }
    }

    public void endOfTurn() {
        for (Effect effect : activeEffects.getEffects()) {
            DataRecord params = effect.getParams();
            
            if (params.has(Keys.DAMAGE_OVER_TIME)) {
                applyDamage(params, 1f);
            }
        }

        activeEffects.decrementDuration();
        activeEffects.getExpiredEffects().forEach(this::removeEffect);
    }

    public void removeEffect(Effect effect) {
        if (!activeEffects.remove(effect)) return;

        System.out.printf("[-] %s\n", effect.getName());

        DataRecord params = effect.getParams();

        //if (params.has(Keys.RECEIVED_DAMAGE_MODIFIER)) {
        //    damageModifiers.removeTemporaryModifier(
        //        params.get(Keys.DAMAGE_TYPE), 
        //        params.get(Keys.RECEIVED_DAMAGE_MODIFIER)
        //    );
        //}
    }
}

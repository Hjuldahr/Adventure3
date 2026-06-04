package com.example.adventure.entities;

import java.util.concurrent.ThreadLocalRandom;

import com.example.adventure.categories.AbilityScores;
import com.example.adventure.categories.DamageModifiers;
import com.example.adventure.categories.DamageTypes;
import com.example.adventure.combat.CombatContext;
import com.example.adventure.context.DataRecord;
import com.example.adventure.context.Keys;
import com.example.adventure.effect.ActiveEffects;
import com.example.adventure.effect.Effect;
import com.example.adventure.events.EventListener;
import com.example.adventure.events.EventTypes;

public abstract class Entity {
    protected final float CRIT_THRESHOLD = 0.05f;
    protected final float GLANCE_THRESHOLD = 0.05f;
    protected final float CRIT_BONUS = 1.5f;
    protected final float GLANCE_PENALTY = 0.5f;
    
    protected String name;
    protected Pronouns pronoun;
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

    public Entity(String name, Pronouns pronoun, int maxHP) {
        this.name = name;
        this.pronoun = pronoun;

        this.hitPoints = new Resource(maxHP);
        this.eventListener = new EventListener();
        this.activeEffects = new ActiveEffects();
        this.attackModifiers = new DamageModifiers();
        this.defenceModifiers = new DamageModifiers();
        this.abilityScores = new AbilityScores();
    }

    public Entity(Entity other) {
        this.name = other.name;
        this.pronoun = other.pronoun;

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
        return 100f / (100f + getDefenceBonus());
    }

    protected abstract int getDefenceBonus();

    private float getEvasion() {
        return evasion;
    }

    public void performWeaponAttack(DataRecord params) {
        Entity target = params.get(Keys.TARGET);

        float evasion = target.getEvasion();
        float denominator = accuracy + evasion;
        float hitThreshold = (denominator <= 0) ? 0.5f : (accuracy / denominator);
        
        float attack = ThreadLocalRandom.current().nextFloat();
        float luck = ThreadLocalRandom.current().nextFloat();

        boolean isHit = attack <= hitThreshold;
        boolean isGlance = !isHit && (luck <= GLANCE_THRESHOLD);

        if (!isHit && !isGlance) {
            System.out.println("Miss!");
            return; 
        }

        DataRecord temp = new DataRecord(params);
        int damage = temp.get(Keys.ATTACK_POWER);

        if (isHit) {
            if (luck <= CRIT_THRESHOLD) {
                damage = (int) Math.ceil(damage * CRIT_BONUS);
                temp.set(Keys.IS_CRITICAL_HIT);
                System.out.printf("%s critically hits %s using %s.\n", 
                    name, target.name, temp.get(Keys.ATTACK_NAME)
                );
            } else {
                System.out.printf("%s hits %s using %s.\n", 
                    name, target.name, temp.get(Keys.ATTACK_NAME)
                );
            }
        } else { 
            damage = (int) Math.ceil(damage * GLANCE_PENALTY);
            temp.set(Keys.IS_GLANCING_HIT);
            System.out.printf("%s glancingly hits %s using %s.\n", 
                name, target.name, temp.get(Keys.ATTACK_NAME)
            );
        }
        
        temp.set(Keys.DAMAGE_TAKEN, damage);
        target.receiveWeaponDamage(temp);
    }

    public void performSpellAttack(DataRecord params) {
        DataRecord temp = new DataRecord(params);
        Entity target = temp.get(Keys.TARGET);
        
        float luck = ThreadLocalRandom.current().nextFloat();
        int damage = temp.get(Keys.ATTACK_POWER);

        if (luck <= CRIT_THRESHOLD) {
            damage = (int) Math.ceil(damage * CRIT_BONUS);
            temp.set(Keys.IS_CRITICAL_HIT);
            System.out.printf("%s strongly casts at %s with %s.\n", 
                name, target.name, temp.get(Keys.ATTACK_NAME)
            );

            //System.out.printf("%s critically casts %s against %s!%n", name, temp.get(Keys.ATTACK_NAME), target.name);
        } else if (luck >= 1f - GLANCE_THRESHOLD) {
            damage = (int) Math.ceil(damage * GLANCE_PENALTY);
            temp.set(Keys.IS_GLANCING_HIT);
            System.out.printf("%s weakly casts at %s with %s.\n", 
                name, target.name, temp.get(Keys.ATTACK_NAME)
            );

        } else {
            System.out.printf("%s casts at %s with %s.\n", 
                name, target.name, temp.get(Keys.ATTACK_NAME)
            );
        }
        
        temp.set(Keys.DAMAGE_TAKEN, damage);
        target.receiveSpellDamage(temp);
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

        System.out.printf("%s takes %s %s damage!\n", name, finalDamage, params.get(Keys.DAMAGE_TYPE));

        eventListener.fire(EventTypes.TAKE_DAMAGE, params);
        hitPoints.change(-finalDamage);
    }

    public void performHeal(DataRecord params) {
        DataRecord temp = new DataRecord(params);
        Entity target = params.get(Keys.TARGET);

        float luck = ThreadLocalRandom.current().nextFloat();
        int healing = temp.get(Keys.HEALING_POWER);

        if (luck <= CRIT_THRESHOLD) {
            healing = (int) Math.ceil(healing * CRIT_BONUS);
            temp.set(Keys.IS_CRITICAL_HEAL);
            System.out.printf("%s strongly casts at %s with %s.\n", 
                name, target.name, temp.get(Keys.ATTACK_NAME)
            );

        } else if (luck >= 1f - GLANCE_THRESHOLD) {
            healing = (int) Math.ceil(healing * GLANCE_PENALTY);
            temp.set(Keys.IS_WEAK_HEAL);
            System.out.printf("%s weakly casts at %s with %s.\n", 
                name, target.name, temp.get(Keys.ATTACK_NAME)
            );

        } else {
            System.out.printf("%s casts at %s with %s.\n", 
                name, target.name, temp.get(Keys.ATTACK_NAME)
            );
        }

        temp.set(Keys.HEALING_TAKEN, healing);
        target.receiveHeal(temp);
    }

    public abstract void middleOfTurn(CombatContext combatContext);

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
        
        if (params.has(Keys.RECEIVED_DAMAGE_MODIFIER)) {
            defenceModifiers.addTemporaryModifier(
                params.get(Keys.DAMAGE_TYPE), 
                params.get(Keys.RECEIVED_DAMAGE_MODIFIER)
            );
        }
        else if (params.has(Keys.DEALT_DAMAGE_MODIFIER)) {
            attackModifiers.addTemporaryModifier(
                params.get(Keys.DAMAGE_TYPE), 
                params.get(Keys.DEALT_DAMAGE_MODIFIER)
            );
        }

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

        if (params.has(Keys.RECEIVED_DAMAGE_MODIFIER)) {
            defenceModifiers.removeTemporaryModifier(
                params.get(Keys.DAMAGE_TYPE), 
                params.get(Keys.RECEIVED_DAMAGE_MODIFIER)
            );
        }
        else if (params.has(Keys.DEALT_DAMAGE_MODIFIER)) {
            attackModifiers.removeTemporaryModifier(
                params.get(Keys.DAMAGE_TYPE), 
                params.get(Keys.DEALT_DAMAGE_MODIFIER)
            );
        }
    }
}

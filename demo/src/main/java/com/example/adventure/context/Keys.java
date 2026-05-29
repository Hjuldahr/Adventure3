package com.example.adventure.context;

import com.example.adventure.categories.DamageTypes;
import com.example.adventure.entities.Entity;

public abstract class Keys {
    public static final ContextKey<Entity> TARGET = ContextKey.named("target");
    public static final ContextKey<DamageTypes> DAMAGE_TYPE = ContextKey.named("damageType");

    public static final ContextKey<Integer> HEALING_TAKEN = ContextKey.named("healingTaken");
    public static final ContextKey<Integer> DAMAGE_TAKEN = ContextKey.named("damageTaken");
    public static final ContextKey<Integer> HEALING_POWER = ContextKey.named("healingPower");
    public static final ContextKey<Integer> DAMAGE_OVER_TIME = ContextKey.named("damageOverTime");
    public static final ContextKey<Integer> HEALING_OVER_TIME = ContextKey.named("healingOverTime");

    public static final ContextKey<Integer> ATTACK_POWER = ContextKey.named("attackPower");
    public static final ContextKey<String> ATTACK_NAME = ContextKey.named("attackName");

    public static final ContextKey<Flag> IS_CRITICAL_HIT = ContextKey.named("isCritHit");
    public static final ContextKey<Flag> IS_GLANCING_HIT = ContextKey.named("isGlanceHit");
    public static final ContextKey<Flag> IS_CRITICAL_HEAL = ContextKey.named("isCritHeal");
    public static final ContextKey<Flag> IS_WEAK_HEAL = ContextKey.named("isWeakHeal");

    public static final ContextKey<Float> RECEIVED_DAMAGE_MODIFIER = ContextKey.named("receivedDamageModifier");
    public static final ContextKey<Float> DEALT_DAMAGE_MODIFIER = ContextKey.named("dealtDamageModifier");
}

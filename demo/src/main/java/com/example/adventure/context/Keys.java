package com.example.adventure.context;

import com.example.adventure.categories.DamageTypes;
import com.example.adventure.entities.Entity;

public abstract class Keys {
    public static final ContextKey<Entity> TARGET = ContextKey.of("target", Entity.class);
    public static final ContextKey<DamageTypes> DAMAGE_TYPE = ContextKey.of("damageType", DamageTypes.class);

    public static final ContextKey<Integer> HEALING_TAKEN = ContextKey.of("healingTaken", Integer.class);
    public static final ContextKey<Integer> DAMAGE_TAKEN = ContextKey.of("damageTaken", Integer.class);
    public static final ContextKey<Integer> HEALING_POWER = ContextKey.of("healingPower", Integer.class);
    public static final ContextKey<Integer> DAMAGE_OVER_TIME = ContextKey.of("damageOverTime", Integer.class);
    public static final ContextKey<Integer> HEALING_OVER_TIME = ContextKey.of("healingOverTime", Integer.class);

    public static final ContextKey<Integer> ATTACK_POWER = ContextKey.of("attackPower", Integer.class);
    public static final ContextKey<String> ATTACK_NAME = ContextKey.of("attackName", String.class);

    public static final ContextKey<Flag> IS_CRITICAL_HIT = ContextKey.of("isCritHit", Flag.class);
    public static final ContextKey<Flag> IS_GLANCING_HIT = ContextKey.of("isGlanceHit", Flag.class);
    public static final ContextKey<Flag> IS_CRITICAL_HEAL = ContextKey.of("isCritHeal", Flag.class);
    public static final ContextKey<Flag> IS_WEAK_HEAL = ContextKey.of("isWeakHeal", Flag.class);

    public static final ContextKey<Float> RECEIVED_DAMAGE_MODIFIER = ContextKey.of("receivedDamageModifier", Float.class);
    public static final ContextKey<Float> DEALT_DAMAGE_MODIFIER = ContextKey.of("dealtDamageModifier", Float.class);
}

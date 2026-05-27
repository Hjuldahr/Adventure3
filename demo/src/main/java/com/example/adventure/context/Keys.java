package com.example.adventure.context;

import com.example.adventure.categories.DamageTypes;
import com.example.adventure.entities.Entity;

public abstract class Keys {
    public static final ContextKey<Entity> TARGET = ContextKey.of("target", Entity.class);
    public static final ContextKey<DamageTypes> DAMAGE_TYPE = ContextKey.of("damageType", DamageTypes.class);

    public static final ContextKey<Integer> HEALING_TAKEN = ContextKey.of("healingTaken", Integer.class);
    public static final ContextKey<Integer> DAMAGE_TAKEN = ContextKey.of("damageTaken", Integer.class);
    public static final ContextKey<Integer> DAMAGE_OVER_TIME = ContextKey.of("damageOverTime", Integer.class);
    public static final ContextKey<Integer> HEALING_OVER_TIME = ContextKey.of("healingOverTime", Integer.class);

    public static final ContextKey<Integer> ATTACK_POWER = ContextKey.of("attackPower", Integer.class);

    public static final ContextKey<Flag> IS_CRITICAL_HIT = ContextKey.of("isCritical", Flag.class);
}

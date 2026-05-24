package com.example.adventure.context;

import com.example.adventure.categories.DamageTypes;
import com.example.adventure.entities.Entity;

public abstract class Keys {
    public static final ContextKey<Integer> APPLY_DAMAGE = ContextKey.of("applyDamage", Integer.class);

    public static final ContextKey<Integer> APPLY_HEAL = ContextKey.of("applyHeal", Integer.class);

    public static final ContextKey<Entity> TARGET = ContextKey.of("target", Entity.class);
    public static final ContextKey<DamageTypes> DAMAGE_TYPE = ContextKey.of("damageType", DamageTypes.class);

    public static final ContextKey<Float> RECEIVED_DAMAGE_MODIFIER = ContextKey.of("receivedDamageModifier", Float.class);
}

package com.example.adventure.context;

import com.example.adventure.categories.DamageTypes;
import com.example.adventure.entities.Entity;

public abstract class Keys {
    public static final ContextKey<Integer> DAMAGE = ContextKey.of("damage", Integer.class);
    public static final ContextKey<Integer> EFFECTIVE_DAMAGE = ContextKey.of("effectiveDamage", Integer.class);

    public static final ContextKey<Integer> HEAL = ContextKey.of("heal", Integer.class);
    public static final ContextKey<Integer> EFFECTIVE_HEAL = ContextKey.of("effectiveHeal", Integer.class);

    public static final ContextKey<Entity> TARGET = ContextKey.of("target", Entity.class);
    public static final ContextKey<DamageTypes> DAMAGE_TYPE = ContextKey.of("damageType", DamageTypes.class);
}

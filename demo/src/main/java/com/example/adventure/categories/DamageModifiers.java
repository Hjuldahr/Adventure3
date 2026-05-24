package com.example.adventure.categories;

import java.util.EnumMap;

public class DamageModifiers {
    private EnumMap<DamageTypes, Float> baseModifier;
    private EnumMap<DamageTypes, Float> temporaryModifiers;
    private EnumMap<DamageTypes, Float> overrideModifiers;

    public DamageModifiers() {
        baseModifier = new EnumMap<>(DamageTypes.class);
        temporaryModifiers = new EnumMap<>(DamageTypes.class);
        overrideModifiers = new EnumMap<>(DamageTypes.class);
    }

    public DamageModifiers(DamageModifiers other) {
        baseModifier = new EnumMap<>(other.baseModifier);
        temporaryModifiers = new EnumMap<>(other.temporaryModifiers);
        overrideModifiers = new EnumMap<>(other.overrideModifiers);
    }

    public float getModifier(DamageTypes damageType) {
        if (overrideModifiers.containsKey(damageType)) {
            return overrideModifiers.get(damageType);
        }
        return baseModifier.getOrDefault(damageType, 1f) + temporaryModifiers.getOrDefault(damageType, 0f);
    }

    public void setBaseModifier(DamageTypes damageType, float modifier) {
        if (modifier == 0f) {
            baseModifier.remove(damageType);
        } else {
            baseModifier.put(damageType, modifier);
        }
    }

    public void addTemporaryModifier(DamageTypes damageType, float modifier) {
        temporaryModifiers.merge(damageType, modifier, Float::sum);
    }

    public void removeTemporaryModifier(DamageTypes damageType, float modifier) {
        temporaryModifiers.merge(damageType, -modifier, Float::sum);
    }

    public void resetTemporaryModifier(DamageTypes damageType) {
        temporaryModifiers.remove(damageType);
    }

    public void setOverrideModifier(DamageTypes damageType, float modifier) {
        if (modifier == 0f) {
            overrideModifiers.remove(damageType);
        } else {
            overrideModifiers.put(damageType, modifier);
        }
    }

    public void resetOverrideModifier(DamageTypes damageType, float modifier) {
        overrideModifiers.remove(damageType);
    }
}

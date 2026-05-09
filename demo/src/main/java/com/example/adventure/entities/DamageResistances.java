package com.example.adventure.entities;

import java.util.EnumMap;
import java.util.stream.Collectors;

public class DamageResistances {
    // 100% -> x0 = no damage
    // 0% -> x1 = normal damage
    // -100% -> x2 = double damage
    
    private EnumMap<DamageTypes,Float> resistances;

    public DamageResistances() {
        this.resistances = new EnumMap<>(DamageTypes.class);
    }

    public DamageResistances(DamageResistances other) {
        this.resistances = new EnumMap<>(other.resistances);
    }

    public float getRawResistance(DamageTypes damageType) {
        return resistances.getOrDefault(damageType, 0f);
    }
    public float getResistanceModifier(DamageTypes damageType) {
        return 1f - resistances.getOrDefault(damageType, 0f);
    }
    public DamageResistances setResistance(DamageTypes damageType, float resistanceValue) {
        if (resistanceValue == 0f) {
            resistances.remove(damageType);
        } else {
            resistances.put(damageType, Math.clamp(resistanceValue, -1f, 1f));    
        }
        return this;
    }

    public DamageResistances setSusceptibility(DamageTypes damageType, float susceptibilityValue) {
        return setResistance(damageType, 1f - susceptibilityValue);
    }

    public void merge(DamageResistances other) {
        other.resistances.forEach((k, v) -> {
            this.resistances.merge(k, v, (a, b) -> {
                float newValue = Math.clamp(a + b, -1f, 1f);
                return newValue == 0f ? null : newValue;
            });
        });
    }

    @Override
    public String toString() {
        if (resistances.isEmpty()) return "No Resistances.";
        
        return resistances.entrySet().stream()
            .map(e -> "%s %.2f%%".formatted(e.getKey(), e.getValue() * 100))
            .collect(Collectors.joining("\n"));
    }
}
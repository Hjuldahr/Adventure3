package com.example.adventure.combat;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

import com.example.adventure.randomizer.Dice;
import com.example.adventure.randomizer.Dice.RollTypes;

public class DamageDicePool {
    // example 1d10 Slashing + 1d4 Refulgent (Blessed)

    private final EnumMap<DamageTypes,Dice> damageDice;
    private final EnumSet<SubDamageTypes> subDamageTypes;

    public DamageDicePool(EnumMap<DamageTypes,Dice> damageDice) {
        this(damageDice, EnumSet.noneOf(SubDamageTypes.class));
    }
    
    public DamageDicePool(EnumMap<DamageTypes,Dice> damageDice, EnumSet<SubDamageTypes> subDamageTypes) {
        this.damageDice = new EnumMap<>(damageDice);
        this.subDamageTypes = EnumSet.copyOf(subDamageTypes);
    }

    public DamageDicePool(DamageDicePool other) {
        this(other.damageDice, other.subDamageTypes);
    }

    public EnumMap<DamageTypes,Dice> getDamageDice() {
        return this.damageDice;
    }

    public boolean hasDamageType(DamageTypes damageType) {
        return damageDice.containsKey(damageType);
    }

    public boolean hasSubDamageType(SubDamageTypes damageType) {
        return subDamageTypes.contains(damageType);
    }

    public Dice getDamageDiceByType(DamageTypes damageType) {
        return damageDice.get(damageType);
    }

    public EnumMap<DamageTypes,Integer> rollDamage() {
        return rollDamage(RollTypes.STANDARD);
    }

    public EnumMap<DamageTypes,Integer> rollDamage(RollTypes rollType) {
        EnumMap<DamageTypes,Integer> results = new EnumMap<>(DamageTypes.class);
        damageDice.forEach((k, v) -> results.put(k, v.roll(rollType)));
        return results;
    }

    @Override
    public String toString() {
        List<String> parts = new ArrayList<>();
        damageDice.forEach((type, dice) -> parts.add(dice.toString() + " " + type.toString()));
        
        String output = String.join(" + ", parts);
        if (!subDamageTypes.isEmpty()) {
            output += " (" + subDamageTypes.toString() + ")";
        }
        return output;
    }

    public boolean isMagical() {
        // True if any die type is magical OR any sub-type (like Enchanted) is magical
        boolean diceMagical = damageDice.keySet().stream().anyMatch(DamageTypes::isMagical);
        boolean subMagical = subDamageTypes.stream().anyMatch(SubDamageTypes::isMagical);
        return diceMagical || subMagical;
    }

    public DamageDicePool merge(DamageDicePool other) {
        EnumMap<DamageTypes, Dice> combinedDice = new EnumMap<>(this.damageDice);
        EnumSet<SubDamageTypes> combinedSubs = EnumSet.copyOf(this.subDamageTypes);
        
        combinedDice.putAll(other.damageDice);
        combinedSubs.addAll(other.subDamageTypes);

        return new DamageDicePool(combinedDice, combinedSubs);
    }
}
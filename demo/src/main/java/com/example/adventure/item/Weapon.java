package com.example.adventure.item;

import com.example.adventure.utility.DicePool;

public class Weapon extends Item {
    private enum WieldingConstraints {
        LIGHT, VERSATILE, HEAVY
    }

    private DicePool standardDamage;
    private DicePool alternateDamage;

    private WieldingConstraints wieldingConstraint;
    
    public Weapon(String name, WieldingConstraints wieldingConstraint, DicePool damageDice) {
        this(name, wieldingConstraint, damageDice, null);
    }
    public Weapon(String name, WieldingConstraints wieldingConstraint, DicePool damageDice, DicePool altDamageDice) {
        super(name, "wielding");

        this.wieldingConstraint = wieldingConstraint;
        this.standardDamage = damageDice;
        this.alternateDamage = altDamageDice;
    }

    @Override
    public boolean requiresBoth() {
        return wieldingConstraint == WieldingConstraints.HEAVY;
    }

    @Override
    public boolean canOff() {
        return wieldingConstraint == WieldingConstraints.LIGHT;
    }

    @Override
    public boolean canMain() {
        return wieldingConstraint == WieldingConstraints.LIGHT || wieldingConstraint == WieldingConstraints.VERSATILE;
    }

    @Override
    public boolean isVersatile() {
        return wieldingConstraint == WieldingConstraints.VERSATILE;
    }

    public DicePool getDamageDice(boolean currentlyTwoHanded) {
        // If we are two-handing a weapon that supports it, try to use the alt damage
        if (currentlyTwoHanded && (requiresBoth() || isVersatile())) {
            return (alternateDamage != null) ? alternateDamage : standardDamage;
        }
        return standardDamage;
    }
}

package com.example.adventure.item;

import java.util.HashSet;
import java.util.List;

public class Armoury {
    private static final int BASE_AC = 10;
    
    private Weapon equippedWeapon;
    private Weapon equippedOffWeapon;
    private Armour donnedArmour;
    private Shield equippedShield;

    private List<Weapon> weapons;
    
    public int getArmourClass() {
        int ac = donnedArmour != null ? donnedArmour.getArmourClass() : BASE_AC;
        ac += equippedShield != null ? equippedShield.getArmourClass() : 0;
        return ac;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public Weapon getEquippedOffWeapon() {
        return equippedOffWeapon;
    }

    public Armour getDonnedArmour() {
        return donnedArmour;
    }

    public Shield getEquippedShield() {
        return equippedShield;
    }

    public void equipWeapon(Weapon weapon, boolean useBothHands) {
        if (weapon.isTwoHandedOnly() || (weapon.isVersatile() && useBothHands)) {
            unequipOffWeapon();
            equippedOffWeapon = weapon;
        }
        unequipWeapon();
        equippedWeapon = weapon;
    }

    public void equipOffWeapon(Weapon weapon) {
        if (weapon.isTwoHandedOnly()) {
            unequipWeapon();
            equippedWeapon = weapon;
        }
        unequipOffWeapon();
        equippedOffWeapon = weapon;
    }

    public void equipShield(Shield shield) {
        if (equippedOffWeapon.isTwoHandedOnly()) {
            unequipOffWeapon();
            equippedWeapon = weapon;
        }
        unequipOffWeapon();
        equippedOffWeapon = weapon;
    }

    public void unequipWeapon() {
        if (equippedWeapon != null) {
            if (equippedOffWeapon.isTwoHandedOnly()) {
                equippedOffWeapon = null;
            }
            weapons.add(equippedWeapon);
            equippedWeapon = null;
        }
    }

    public void unequipOffWeapon() {
        if (equippedOffWeapon != null) {
            if (equippedWeapon.isTwoHandedOnly()) {
                equippedWeapon = null;
            }
            weapons.add(equippedOffWeapon);
            equippedOffWeapon = null;
        }
    }
}

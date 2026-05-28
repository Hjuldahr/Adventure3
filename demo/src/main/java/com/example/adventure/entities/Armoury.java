package com.example.adventure.entities;

import java.util.List;

import com.example.adventure.items.Armour;
import com.example.adventure.items.Shield;
import com.example.adventure.items.Weapon;

public class Armoury {
    private Weapon activePrimaryWeapon;
    private Weapon activeSecondaryWeapon;
    private Armour activeArmour;
    private Shield activeShield;

    private List<Weapon> storedWeapons;
    private List<Armour> storedArmour;
    private List<Shield> storedShields;

    public void equipWeapon(Weapon weapon) {
        unequipSecondary();
        
        // if current or new weapon is two handed, swap to new weapon
        if (weapon.getTwoHanded() || (activePrimaryWeapon != null && activePrimaryWeapon.getTwoHanded())) {
            unequipPrimary();

        // if current main weapon is not two handed, stow current secondary then shift primary to secondary
        } else if (activePrimaryWeapon != null) {
            activeSecondaryWeapon = activePrimaryWeapon;
        }

        activePrimaryWeapon = weapon;
    }

    private void unequipPrimary() {
        if (activePrimaryWeapon == null) return;
        storedWeapons.add(activePrimaryWeapon);
        activePrimaryWeapon = null;
    }

    private void unequipSecondary() {
        if (activeSecondaryWeapon != null) {
            storedWeapons.add(activeSecondaryWeapon);
            activeSecondaryWeapon = null;
        }
        if (activeShield != null) {
            storedShields.add(activeShield);
            activeShield = null;
        }
    }

    public void equipShield(Shield shield) {
        if (activeShield != null) {
            unequipShield();
        }
        if (activeWeapon != null && activeWeapon.getTwoHanded()) {
            unequipWeapon();
        }
        activeShield = shield;
    }
}

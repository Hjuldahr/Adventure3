package com.example.adventure.entities;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.example.adventure.items.Armour;
import com.example.adventure.items.BodyParts;
import com.example.adventure.items.Item;
import com.example.adventure.items.Accessory;
import com.example.adventure.items.Shield;
import com.example.adventure.items.Weapon;

public class Armoury {
    private Weapon activePrimaryWeapon = null;
    private Weapon activeSecondaryWeapon = null;
    private Armour activeArmour = null;
    private Shield activeShield = null;
    private EnumMap<BodyParts,Accessory> activeAccessories = new EnumMap<>(BodyParts.class);

    private final List<Item> stored = new ArrayList<>();

    public void equipWeapon(Weapon weapon) {
        if (!unstore(weapon)) return;

        // remove secondary as it will always get pushed out of the way
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

    public void equipArmour(Armour armour) {
        if (!unstore(armour)) return;
        
        unequipArmour();
        activeArmour = armour;
    }

    public void equipShield(Shield shield) {
        if (!unstore(shield)) return;

        unequipSecondary();

        if (activePrimaryWeapon != null && activePrimaryWeapon.getTwoHanded()) {
            unequipPrimary();
        }
        
        activeShield = shield;
    }

    public void equipAccessory(Accessory accessory) {
        if (!unstore(accessory)) return;

        BodyParts bodyPart = accessory.getAssociatedBodyPart();
        Accessory old = activeAccessories.put(bodyPart, accessory);
        if (old != null) {
            stored.add(old);
        }
    }

    private void unequipPrimary() {
        if (activePrimaryWeapon == null) return;
        stored.add(activePrimaryWeapon);
        activePrimaryWeapon = null;
    }

    private void unequipSecondary() {
        if (activeSecondaryWeapon != null) {
            stored.add(activeSecondaryWeapon);
            activeSecondaryWeapon = null;
        }
        if (activeShield != null) {
            stored.add(activeShield);
            activeShield = null;
        }
    }

    private void unequipArmour() {
        if (activeArmour == null) return;
        stored.add(activeArmour);
        activeArmour = null;
    }

    private boolean unstore(Item item) {
        return stored.remove(item);
    }

    public boolean mainHandOccupied() {
        return activePrimaryWeapon != null;
    }

    public boolean offHandOccupied() {
        return activeSecondaryWeapon != null || activeShield != null;
    }

    public boolean bothHandsOccupied() {
        return mainHandOccupied() && (activePrimaryWeapon.getTwoHanded() || offHandOccupied());
    }
}

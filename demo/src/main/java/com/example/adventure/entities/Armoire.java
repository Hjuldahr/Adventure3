package com.example.adventure.entities;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Stream;

import com.example.adventure.items.Garment;
import com.example.adventure.items.BodyParts;
import com.example.adventure.items.Item;
import com.example.adventure.items.Accessory;
import com.example.adventure.items.Shield;
import com.example.adventure.items.Weapon;

public class Armoire {
    private Weapon activePrimaryWeapon = null;
    private Weapon activeSecondaryWeapon = null;
    private Garment activeGarment = null;
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

    public void equipGarment(Garment garment) {
        if (!unstore(garment)) return;
        
        unequipGarment();
        activeGarment = garment;
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

    private void unequipGarment() {
        if (activeGarment == null) return;
        stored.add(activeGarment);
        activeGarment = null;
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

    public boolean isArmoured() {
        return activeGarment != null || activeShield != null;
    }

    private int getAccessoryArmouredBonus() {
        return activeAccessories.values().stream()
            .mapToInt(Accessory::getArmouredDefenceBonus)
            .sum();
    }

    private int getAccessoryUnarmouredBonus() {
        return activeAccessories.values().stream()
            .mapToInt(Accessory::getUnarmouredDefenceBonus)
            .sum();
    }

    public int getArmouredDefenceBonus() {
        int armour = activeGarment != null ? activeGarment.getDefenceBonus() : 0;
        int shield = activeShield != null ? activeShield.getDefenceBonus() : 0;
        int bonus = getAccessoryArmouredBonus();
        return armour + shield + bonus;
    }

    public int getUnarmouredDefenceBonus() {
        int bonus = getAccessoryUnarmouredBonus();
        return bonus;
    }

    public int getDefenceBonus() {
        return isArmoured() ? getArmouredDefenceBonus() : getUnarmouredDefenceBonus();
    }
}

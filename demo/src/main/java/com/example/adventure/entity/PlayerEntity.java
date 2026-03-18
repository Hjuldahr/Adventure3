package com.example.adventure.entity;

import com.example.adventure.currency.CoinPurse;
import com.example.adventure.entity.AbilityScores.AbilityCategories;
import com.example.adventure.item.Armour;
import com.example.adventure.item.Armoury;
import com.example.adventure.item.Inventory;
import com.example.adventure.item.Item;
import com.example.adventure.item.Shield;

public class PlayerEntity extends Entity
{
    private static final int BASE_AC = 10;
    
    private Armoury armoury;
    private Inventory inventory;
    private CoinPurse coinPurse;

    private Item mainHand = null;
    private Item offHand = null;
    private Armour equippedArmour;
    
    public PlayerEntity(
        String name
    ) {
        super(name);
    }

    @Override
    public int getArmourClass() {
        int armourClass = (equippedArmour != null ? equippedArmour.getArmourClass() : BASE_AC) + abilityScores.getModifier(AbilityCategories.AGILITY);
        
        if (offHand instanceof Shield equippedShield) {
            armourClass += equippedShield.getArmourClass();
        }

        return armourClass;
    }

    @Override
    public void turn() {
        super.turn();
    }

    // Item Logic

    public Inventory getInventory() {
        return inventory;
    }

    public void viewInventory() {
        // Check if hands are empty first
        if (mainHand == null && offHand == null) {
            System.out.println("Your hands are empty.");
        }
        // Handle two-handed items
        else if (mainHand != null && offHand == null && mainHand.requiresBoth()) {
            System.out.printf("You are %s a %s with both-hands.\n", mainHand.getVerb(), mainHand.getName());
        }
        // Handle dual-wielding (both hands full)
        else if (mainHand != null && offHand != null) {
            // Special case: Identical items (e.g., two "Iron Daggers")
            if (mainHand.getName().equals(offHand.getName())) {
                System.out.printf("You are dual-wielding %ss.\n", mainHand.getName());
            } 
            else {
                System.out.printf("You are %s a %s and %s a %s.\n", 
                    mainHand.getVerb(), mainHand.getName(), offHand.getVerb(), offHand.getName());
            }
        } 
        // Single hand cases
        else if (mainHand != null) {
            System.out.printf("You are %s a %s with your main-hand.\n", mainHand.getVerb(), mainHand.getName());
        } 
        else {
            System.out.printf("You are %s a %s with your off-hand.\n", offHand.getVerb(), offHand.getName());
        }

        inventory.viewInventory();
    }

    // Holding Logic
    // Returns item incase you are dropping or trading instead of stowing / weapon swapping

    public Item getMainHand() {
        return mainHand;
    }
    public Item getOffhand() {
        return offHand;
    }
    public void holdItem(Item newItem) {
        if (newItem == null) return;

        // 1. HEAVY: Occupies both hands physically.
        if (newItem.requiresBoth()) {
            stowItem(mainHand);
            stowItem(offHand);
            mainHand = newItem;
            offHand = null;
            return;
        }

        // 2. SHIELD: Strictly off-hand.
        if (newItem.canOff() && !newItem.canMain()) {
            // If holding a Heavy weapon, it must be stowed.
            if (mainHand != null && mainHand.requiresBoth()) {
                stowItem(mainHand);
                mainHand = null;
            }
            stowItem(offHand);
            offHand = newItem;
            return;
        }

        // 3. MAIN-HAND CAPABLE (Light, Other, Versatile)
        if (newItem.canMain()) {
            if (mainHand != null) {
                // SHIFT: Only Light/Other (canOff) can move to the off-hand.
                // Versatile (canMain but !canOff) will be stowed instead.
                if (offHand == null && mainHand.canOff()) {
                    offHand = mainHand;
                } else {
                    stowItem(mainHand);
                }
            }
            mainHand = newItem;
        }
    }

    public boolean isTwoHanding() {
        if (mainHand == null) return false;
        // Heavy forces two hands. Versatile uses both ONLY if off-hand is empty.
        return mainHand.requiresBoth() || (mainHand.isVersatile() && offHand == null);
    }

    public boolean canCastSpell() {
        // Rule: Requires at least one hand free.
        // 1. If either hand is physically empty, you can cast.
        if (mainHand == null || offHand == null) {
            // Exception: Heavy weapons physically occupy both hands even if offHand is null.
            if (mainHand != null && mainHand.requiresBoth()) return false;
            // Versatile weapons are assumed to swap stances, so having no item 
            // in offHand counts as a free hand for casting.
            return true; 
        }
        // 2. If both hands have items (e.g., Sword + Shield or Dual Wielding), no hand is free.
        return false;
    }

    private void stowItem(Item item) {
        if (item != null) {
            inventory.add(item);
        }
    }
}

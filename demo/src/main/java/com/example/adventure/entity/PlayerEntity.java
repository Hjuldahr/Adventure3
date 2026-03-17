package com.example.adventure.entity;

import com.example.adventure.currency.CoinPurse;
import com.example.adventure.entity.AbilityScores.AbilityCategories;
import com.example.adventure.item.Armoury;
import com.example.adventure.item.Inventory;
import com.example.adventure.item.Item;

public class PlayerEntity extends Entity
{
    private Armoury armoury;
    private Inventory inventory;
    private CoinPurse coinPurse;

    private Item mainHand = null;
    private Item offHand = null;
    
    public PlayerEntity(
        String name
    ) {
        super(name);
    }

    @Override
    public int getArmourClass() {
        return armoury.getArmourClass() + abilityScores.getModifier(AbilityCategories.AGILITY);
    }

    @Override
    public void turn() {
        super.turn();
    }

    // Item Logic

    public Inventory getInventory() {
        return inventory;
    }

    // Holding Logic
    // Returns item incase you are dropping or trading instead of stowing / weapon swapping

    public Item getMainHand() {
        return mainHand;
    }
    public Item getOffhand() {
        return offHand;
    }
    public void setMainHand(Item newItem) {
        // 1. If we are currently holding a 2H item, it occupies both hands. 
        //    Move it to inventory and clear both slots.
        if (mainHand != null && mainHand.getRequiresTwoHands()) {
            inventory.add(mainHand);
            mainHand = null;
            offHand = null;
        }

        // 2. If the NEW item is 2H, we must also clear whatever is in the off-hand.
        if (newItem != null && newItem.getRequiresTwoHands()) {
            if (offHand != null) {
                inventory.add(offHand);
            }
            mainHand = newItem;
            offHand = newItem;
        } 
        // 3. If the NEW item is 1H, push the old main-hand item to the off-hand.
        //    BUT, we must save the old off-hand item first!
        else {
            if (offHand != null) {
                inventory.add(offHand); // Save the shield/off-hand item
            }
            offHand = mainHand; // Move old sword to off-hand
            mainHand = newItem; // Put new dagger in main-hand
        }
    }
    public void setOffHand(Item newItem) {
        // 1. If currently two-handing, the whole weapon goes to inventory
        if (offHand != null && offHand.getRequiresTwoHands()) {
            inventory.add(offHand);
            mainHand = null;
            offHand = null;
        }

        // 2. If the NEW item is two-handed, it takes over both slots
        if (newItem != null && newItem.getRequiresTwoHands()) {
            if (mainHand != null) inventory.add(mainHand);
            // (Note: if offHand was different from mainHand, it was already cleared in step 1)
            mainHand = newItem;
            offHand = newItem;
        } 
        // 3. New item is one-handed
        else {
            // If mainHand is already taken, the "pushed" item goes to inventory
            if (mainHand != null && offHand != null) {
                inventory.add(mainHand);
            }
            
            // Push current offHand to mainHand, then set the new item
            mainHand = offHand; 
            offHand = newItem;
        }
    }
    public Item unsetMainHand() {
        Item previous = mainHand;
        if (mainHand != null && mainHand.getRequiresTwoHands()) {
            offHand = null; // Clear both slots for 2H
        }
        mainHand = null;
        return previous;
    }
    public Item unsetOffHand() {
        Item previous = offHand;
        if (offHand != null && offHand.getRequiresTwoHands()) {
            mainHand = null; // Clear both slots for 2H
        }
        offHand = null;
        return previous;
    }
    public void swapHands() {
        // if main hand is occupied but offhand is empty, dont swap
        // if both hands are empty or holding the same thing, dont swap
        if (offHand == null || mainHand == offHand) return;
        
        Item currentItem = mainHand;
        mainHand = offHand;
        offHand = currentItem;
    }
    public boolean hasHandFree() {
        return mainHand == null || offHand == null;
    }
    public boolean hasBothHandsFree() {
        return mainHand == null && offHand == null;
    }
}

package com.example.adventure.creature;

import java.util.Arrays;

public class SpellSlots {
    public static final int MAX_LEVEL_SLOT = 9;
    private int[] totalSpellSlots;
    private int[] availableSpellSlots;
    
    public SpellSlots() {
        this.totalSpellSlots = new int[MAX_LEVEL_SLOT];
        this.availableSpellSlots = new int[MAX_LEVEL_SLOT];
    }
    /**
     * Example: 3, 2, 0, 1 (levels 1-4)
     * @param spellSlotCounts
     */
    public SpellSlots(int... incoming) {
        this();

        int limit = Math.min(incoming.length, MAX_LEVEL_SLOT);

        for (int n = 0; n < limit; n++) {
            this.totalSpellSlots[n] = incoming[n];
            this.availableSpellSlots[n] = incoming[n];
        }
    }

    public boolean hasSpellSlotAtNthLevel(int spellLevel) {
        return this.availableSpellSlots[spellLevel - 1] > 0;
    }

    public void expendSpellSlotAtNthLevel(int spellLevel) {
        expendSpellSlotAtNthLevel(spellLevel, 1);
    }

    public void expendSpellSlotAtNthLevel(int spellLevel, int change) {
        int index = spellLevel-1;
        
        this.availableSpellSlots[index] = Math.max(
            this.availableSpellSlots[index] - change, 
            0
        );
    }

    public void restoreSpellSlotAtNthLevel(int spellLevel) {
        restoreSpellSlotAtNthLevel(spellLevel, 1);
    }

    public void restoreSpellSlotAtNthLevel(int spellLevel, int change) {
        int index = spellLevel-1;

        this.availableSpellSlots[index] = Math.min(
            this.availableSpellSlots[index] + change, 
            this.totalSpellSlots[index]
        );
    }

    public int[] getAvailableSpellSlots() { return this.availableSpellSlots; }
    public int[] getTotalSpellSlots() { return this.totalSpellSlots; }

    public void setAvailableSpellSlots(int[] incoming) { 
        Arrays.fill(this.availableSpellSlots, 0);
        int limit = Math.min(incoming.length, MAX_LEVEL_SLOT);
        
        for (int i = 0; i < limit; i++) {
            int incomingValue = Math.clamp(incoming[i], 0, this.totalSpellSlots[i]);
            this.availableSpellSlots[i] = incomingValue;
        }
    }
    public void setTotalSpellSlots(int[] incoming) { 
        Arrays.fill(this.totalSpellSlots, 0);
        int limit = Math.min(incoming.length, MAX_LEVEL_SLOT);

        for (int i = 0; i < limit; i++) {
            int incomingValue = Math.max(0, incoming[i]);
            this.totalSpellSlots[i] = incomingValue;
        }

        Arrays.setAll(this.availableSpellSlots, i -> Math.min(this.availableSpellSlots[i], this.totalSpellSlots[i]));
    }

    public void restoreAllSpellSlots() {
        Arrays.setAll(this.availableSpellSlots, i -> this.totalSpellSlots[i]);
    }

    public void view() {
        System.out.println("-=-=-=-: Spell Slots :-=-=-=-");
        
        for (int i = 0; i < MAX_LEVEL_SLOT; i++) {
            if (this.totalSpellSlots[i] > 0) {
                System.out.printf("Level %d: %d / %d\n", 
                    i+1, this.availableSpellSlots[i], this.totalSpellSlots[i]
                );
            }
        }
    }
}

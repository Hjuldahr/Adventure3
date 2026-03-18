package com.example.adventure.item;

import com.example.adventure.utility.Colours;

public enum ItemRarities { // ordered so the highest priority items get sorted first
    ARTIFACT("Artifact", Colours.YELLOW), 
    LEGENDARY("Legendary", Colours.YELLOW), 
    VERY_RARE("Very Rare", Colours.MAGENTA), 
    RARE("Rare", Colours.BLUE), 
    UNCOMMON("Uncommon", Colours.GREEN), 
    COMMON("Common", Colours.DEFAULT), 
    JUNK("Junk", Colours.DEFAULT);

    private final String name;
    private final Colours colour;

    ItemRarities(String name, Colours colour) {
        this.name = name;
        this.colour = colour;
    }
    public String getName() {
        return this.name;
    }
    public Colours getColour() {
        return this.colour;
    }
}
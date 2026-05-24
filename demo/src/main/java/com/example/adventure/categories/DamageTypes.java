package com.example.adventure.categories;

public enum DamageTypes 
{
    PHYSICAL("Physical", false),
    POISON("Poison", false),
    FIRE("Fire", true), 
    ICE("Ice", true), 
    WIND("Wind", true),  
    LIGHTNING("Lightning", true),  
    PSIONIC("Psionic", false), 
    DIVINE("Divine", false), 
    VOID("Void", false), 
    ASTRAL("Astral", false);

    private final String name;
    private final boolean isElemental;

    DamageTypes(String name, boolean isElemental) {
        this.name = name;
        this.isElemental = isElemental;
    }

    public String getName() { return name; }
    public boolean isElemental() { return isElemental; }
    @Override
    public String toString() { return name; }
}
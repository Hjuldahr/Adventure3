package com.example.adventure.categories;

public enum DamageTypes 
{
    PHYSICAL("Physical", false, false),
    POISON("Poison", false, true),
    FIRE("Fire", true, false), 
    ICE("Ice", true, false), 
    WIND("Wind", true, false),  
    LIGHTNING("Lightning", true, false),  
    PSIONIC("Psionic", false, true), 
    DIVINE("Divine", false, false), 
    VOID("Void", false, false), 
    ASTRAL("Astral", false, true);

    private final String name;
    private final boolean isElemental;
    private final boolean ignoresArmour;

    DamageTypes(String name, boolean isElemental, boolean ignoresArmour) {
        this.name = name;
        this.isElemental = isElemental;
        this.ignoresArmour = ignoresArmour;
    }

    public String getName() { return name; }
    public boolean isElemental() { return isElemental; }
    public boolean ignoresArmour() { return ignoresArmour; }
    @Override
    public String toString() { return name; }
}
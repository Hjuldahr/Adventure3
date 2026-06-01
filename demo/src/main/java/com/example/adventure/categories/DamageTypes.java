package com.example.adventure.categories;

public enum DamageTypes 
{
    PHYSICAL("Physical"),
    POISON("Poison", DefenceBypass.ARMOUR),
    FIRE("Fire", true), 
    ICE("Ice", true), 
    WIND("Wind", true),  
    LIGHTNING("Lightning", true),  
    PSIONIC("Psionic", DefenceBypass.ARMOUR), 
    DIVINE("Divine"), 
    VOID("Void"), 
    ASTRAL("Astral", DefenceBypass.ALL);

    private final String name;
    private final boolean isElemental;
    private final DefenceBypass defenceBypass;

    DamageTypes(String name) {
        this(name, false, DefenceBypass.NONE);
    }

    DamageTypes(String name, boolean isElemental) {
        this(name, isElemental, DefenceBypass.NONE);
    }

    DamageTypes(String name, DefenceBypass defenceBypass) {
        this(name, false, defenceBypass);
    }

    DamageTypes(String name, boolean isElemental, DefenceBypass defenceBypass) {
        this.name = name;
        this.isElemental = isElemental;
        this.defenceBypass = defenceBypass;
    }

    public String getName() { return name; }
    public boolean isElemental() { return isElemental; }
    public DefenceBypass defenceBypass() { return defenceBypass; }
    @Override
    public String toString() { return name; }
}
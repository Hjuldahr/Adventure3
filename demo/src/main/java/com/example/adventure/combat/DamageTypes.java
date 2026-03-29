package com.example.adventure.combat;

// thunder is reclassified as bludgeoning as wind is a pushing force
public enum DamageTypes {
    BLUDGEONING("Bludgeoning", false), 
    PIERCING("Piercing", false), 
    SLASHING("Slashing", false),

    BURNING("Burning", false), 
    TOXIC("Poison", false), 
    CORRODING("Corroding", false),
    RIME("Rime", false), 
    FULMINOUS("Fulminous", false), 
    PSIONIC("Psionic", true), 
    REFULGENT("Refulgent", true), 
    OBLIVIATING("Obliviating", true), 

    NON_DAMAGE("Undamaging", false);

    private final String name;
    // if its intrinsically magical regardless of source (spell or magic item)
    private final boolean isMagical; 

    DamageTypes(String name, boolean isMagical) {
        this.name = name;
        this.isMagical = isMagical;
    }

    public boolean isMagical() {
        return this.isMagical;
    }

    @Override
    public String toString() {
        return this.name;
    }
};
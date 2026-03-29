package com.example.adventure.combat;

public enum SubDamageTypes {
    METAL("Metal", false), // rust monsters
    SILVERED("Silvered", false),
    MITHRAL("Mithral", false),
    ADAMANTINE("Adamantine", false),

    BLESSED("Blessed", true), // holy fire or holy weapons
    HEXED("Hexed", true), // cursed weapons
    ENCHANTED("Enchanted", true); // general magic weapons

    private final String name;
    // if its intrinsically magical regardless of source (spell or magic item)
    private final boolean isMagical; 

    SubDamageTypes(String name, boolean isMagical) {
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
}
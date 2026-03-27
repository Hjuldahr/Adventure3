package com.example.adventure.creature;

public enum CreatureType {
    ABERRATION("Aberration"),
    BEAST("Beast"),
    CELESTIAL("Celestial"),
    CONSTRUCT("Construct"),
    DRAGON("Dragon"),
    ELEMENTAL("Elemental"),
    FAE("Fae"),
    FIEND("Fiend"),
    GIANT("Giant"),
    HUMANOID("Humanoid"),
    MONSTROSITY("Monstrosity"),
    OOZE("Ooze"),
    PLANT("Plant"),
    UNDEAD("Undead");

    private final String name;

    CreatureType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}

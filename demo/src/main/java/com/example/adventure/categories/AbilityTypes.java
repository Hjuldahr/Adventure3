package com.example.adventure.categories;

public enum AbilityTypes {
    AGILITY("Agility", true),
    BRAWN("Brawn", true),
    FORTITUDE("Fortitude", true),
    INTELLECT("Intellect", false),
    CHARM("Charm", false),
    SPIRIT("Spirit", false);

    private final String name;
    private final boolean isPhysical;

    AbilityTypes(String name, boolean isPhysical) {
        this.name = name;
        this.isPhysical = isPhysical;
    }

    public String getName() { return name; }
    public boolean isPhysical() { return isPhysical; }
    @Override
    public String toString() { return name; }
}
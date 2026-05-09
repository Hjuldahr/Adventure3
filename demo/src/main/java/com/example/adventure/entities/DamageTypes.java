package com.example.adventure.entities;

public enum DamageTypes {
    PHYSICAL("Physical"),
    POISON("Poison"),
    FIRE("Fire"),
    ICE("Ice"),
    WIND("Wind"),
    WATER("Water"),
    LIGHTNING("Lightning"),
    DIVINE("Divine"),
    VOID("Void");

    private final String name;

    private DamageTypes(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() { return name; }
}
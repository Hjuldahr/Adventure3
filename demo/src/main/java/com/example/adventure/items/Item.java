package com.example.adventure.items;

import com.example.adventure.categories.DamageTypes;

public class Item {
    private String name;
    private int buyCost;

    private int speedBonus;

    private int maxHPBonus;

    private int maxMPBonus;
    private int MPRegeneration;

    private int weaponDamageBonus;
    private int spellDamageBonus;
    private int spellHealBonus;

    private float accuracyBonus;
    private float evasionBonus;

    private DamageTypes damageAttackType;
    private float damageAtackModifier;

    private DamageTypes damageDefenceType;
    private float damageDefenceModifier;

    public Item(String name, int buyCost) {
        this.name = name;
        this.buyCost = buyCost;
    }

    public Item(Item other) {
        this.name = other.name;
        this.buyCost = other.buyCost;

        this.speedBonus = other.speedBonus;

        this.maxHPBonus = other.maxHPBonus;

        this.maxMPBonus = other.maxMPBonus;
        this.MPRegeneration = other.MPRegeneration;

        this.weaponDamageBonus = other.weaponDamageBonus;
        this.spellDamageBonus = other.spellDamageBonus;
        this.spellHealBonus = other.spellHealBonus;

        this.accuracyBonus = other.accuracyBonus;
        this.evasionBonus = other.evasionBonus;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }
    public String getName() { return name; }

    public Item setBuyCost(int buyCost) {
        this.buyCost = buyCost;
        return this;
    }
    public int getBuyCost() { return buyCost; }
    public int getSellCost() { return buyCost / 2; }

    public Item setSpeedBonus(int speedBonus) {
        this.speedBonus = speedBonus;
        return this;
    }
    public int getSpeedBonus() { return speedBonus; }

    public Item setMaxHPBonus(int maxHPBonus) {
        this.maxHPBonus = maxHPBonus;
        return this;
    }
    public int getMaxHPBonus() { return maxHPBonus; }

    public Item setMaxMPBonus(int maxMPBonus) {
        this.maxMPBonus = maxMPBonus;
        return this;
    }
    public int getMaxMPBonus() { return maxMPBonus; }

    public Item setMPRegeneration(int MPRegeneration) {
        this.MPRegeneration = MPRegeneration;
        return this;
    }
    public int getMPRegeneration() { return MPRegeneration; }

    public Item setWeaponDamageBonus(int weaponDamageBonus) {
        this.weaponDamageBonus = weaponDamageBonus;
        return this;
    }
    public int getWeaponDamageBonus() { return weaponDamageBonus; }

    public Item setSpellDamageBonus(int spellDamageBonus) {
        this.spellDamageBonus = spellDamageBonus;
        return this;
    }
    public int getSpellDamageBonus() { return spellDamageBonus; }

    public Item setSpellHealBonus(int spellHealBonus) {
        this.spellHealBonus = spellHealBonus;
        return this;
    }
    public int getSpellHealBonus() { return spellHealBonus; }

    public Item setAccuracyBonus(float accuracyBonus) {
        this.accuracyBonus = accuracyBonus;
        return this;
    }
    public float getAccuracyBonus() { return accuracyBonus; }

    public Item setEvasionBonus(float evasionBonus) {
        this.evasionBonus = evasionBonus;
        return this;
    }
    public float getEvasionBonus() { return evasionBonus; }
}

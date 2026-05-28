package com.example.adventure.items;

import java.util.Optional;
import com.example.adventure.context.DataRecord;

public class Weapon extends Equipment {
    private int attackPower = 0;
    private boolean isTwoHanded = false;

    private WeaponMasteryTrait weaponMasteryTrait; // added to the attack

    public Weapon(String name, int buyCost, int level) {
        super(name, buyCost, level);
    }

    public Weapon(Weapon other) {
        super(other);
        this.isTwoHanded = other.isTwoHanded;
        this.weaponMasteryTrait = other.weaponMasteryTrait;
    }

    public Weapon setAttackPower(int attackPower) {
        this.attackPower = attackPower;
        return this;
    }
    public int getAttackPower() { return attackPower; }

    public Weapon setTwoHanded(boolean isTwoHanded) {
        this.isTwoHanded = isTwoHanded;
        return this;
    }
    public boolean getTwoHanded() { return isTwoHanded; }

    public Weapon setWeaponMasteryTrait(WeaponMasteryTrait weaponMasteryTrait) {
        this.weaponMasteryTrait = weaponMasteryTrait;
        return this;
    }
    public WeaponMasteryTrait getWeaponMasteryTrait() { 
        return weaponMasteryTrait; 
    }

    public Optional<DataRecord> useWeaponMaster(DataRecord params) {
        DataRecord temp = new DataRecord(params);
        if (isMastered) {
            return Optional.of(weaponMasteryTrait.trigger(temp));
        }
        return Optional.empty();
    }
}
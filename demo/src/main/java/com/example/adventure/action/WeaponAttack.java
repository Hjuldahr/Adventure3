package com.example.adventure.action;

import com.example.adventure.combat.DamageTypes;
import com.example.adventure.utility.Dice;

public class WeaponAttack extends Action {
    public static final WeaponAttack D4_BLUDGEONING = new WeaponAttack(
        Dice.d4(), DamageTypes.BLUDGEONING
    );
    public static final WeaponAttack D4_PIERCING = new WeaponAttack(
        Dice.d4(), DamageTypes.PIERCING
    );
    public static final WeaponAttack D4_SLASHING = new WeaponAttack(
        Dice.d4(), DamageTypes.SLASHING
    );
    public static final WeaponAttack D6_2_BLUDGEONING = new WeaponAttack(
        new Dice(2, 6), DamageTypes.BLUDGEONING
    );
    public static final WeaponAttack D6_2_SLASHING = new WeaponAttack(
        new Dice(2, 6), DamageTypes.SLASHING
    );
    public static final WeaponAttack D6_BLUDGEONING = new WeaponAttack(
        Dice.d6(), DamageTypes.BLUDGEONING
    );
    public static final WeaponAttack D6_PIERCING = new WeaponAttack(
        Dice.d6(), DamageTypes.PIERCING
    );
    public static final WeaponAttack D6_SLASHING = new WeaponAttack(
        Dice.d6(), DamageTypes.SLASHING
    );
    public static final WeaponAttack D8_BLUDGEONING = new WeaponAttack(
        Dice.d8(), DamageTypes.BLUDGEONING
    );
    public static final WeaponAttack D8_PIERCING = new WeaponAttack(
        Dice.d8(), DamageTypes.PIERCING
    );
    public static final WeaponAttack D8_SLASHING = new WeaponAttack(
        Dice.d8(), DamageTypes.SLASHING
    );
    public static final WeaponAttack D10_PIERCING = new WeaponAttack(
        Dice.d10(), DamageTypes.PIERCING
    );
    public static final WeaponAttack D10_SLASHING = new WeaponAttack(
        Dice.d10(), DamageTypes.SLASHING
    );
    public static final WeaponAttack D12_PIERCING = new WeaponAttack(
        Dice.d12(), DamageTypes.PIERCING
    );
    public static final WeaponAttack D12_SLASHING = new WeaponAttack(
        Dice.d12(), DamageTypes.SLASHING
    );
    public static final WeaponAttack F1_PIERCING = new WeaponAttack(
        1, DamageTypes.PIERCING
    );
    public static final WeaponAttack NONE = new WeaponAttack(
        0, DamageTypes.NONE
    );

    private final Dice damageDice;
    private final int damageBonus;

    public WeaponAttack(Dice damageDice, DamageTypes damageType) {
        this(damageDice, 0, damageType);
    }
    
    public WeaponAttack(Dice damageDice, int damageBonus, DamageTypes damageType) {
        this.damageDice = damageDice;
        this.damageBonus = damageBonus;
    }

    public WeaponAttack(int flatDamage, DamageTypes damageType) {
        this(Dice.d0(), flatDamage, damageType);
    }
}

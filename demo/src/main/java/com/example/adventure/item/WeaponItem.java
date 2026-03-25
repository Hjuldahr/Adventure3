package com.example.adventure.item;

import com.example.adventure.action.AttackAction.AttackRange;
import com.example.adventure.combat.DamageTypes;

import java.util.Arrays;
import java.util.EnumSet;

import com.example.adventure.action.AttackAction;
import com.example.adventure.action.WeaponAttack;
import com.example.adventure.utility.Dice;

public class WeaponItem extends Item {
    public enum WeaponProperties {
        LIGHT("Light"), 
        FINESSE("Finesse"), 
        THROWN("Thrown"), 
        TWO_HANDED("Two Handed"), 
        VERSATILE("Versatile"), 
        LOADING("Loading"), 
        HEAVY("Heavy"), 
        REACH("Reach"), 
        RANGE("Range"),
        SPECIAL("Special"), 
        AMMUNITION_BOLT("Ammunition: Bolt"), 
        AMMUNITION_ARROW("Ammunition: Arrow"), 
        AMMUNITION_STONE("Ammunition: Stone"), 
        AMMUNITION_NEEDLE("Ammunition: Needle");

        private final String name;
        private final boolean isAmmo;

        WeaponProperties(String name) {
            this.name = name;
            this.isAmmo = name.startsWith("Ammunition");
        }
        public String getName() {
            return name;
        }
        public boolean isAmmunition() {
            return isAmmo;
        }
    }
    protected final AttackAction attack;
    protected final AttackAction altAttack;
    protected final AttackRange range;
    protected final EnumSet<WeaponProperties> properties;
    protected final int weight;
    protected final int currencyAmount;
    protected final CurrencyTypes currencyType;

    protected final boolean isVersatile;
    protected final boolean isHeavy;
    protected final boolean canMainHand;
    protected final boolean canOffHand;
    protected final boolean requiresAmmunition;
    protected final boolean canLoad;
    protected boolean isLoaded;
    
    public WeaponItem(String name, int currencyAmount, CurrencyTypes currencyType, Dice damageDice, DamageTypes damageType, int weight, WeaponProperties... properties) {
        this(
            name, 
            currencyAmount, currencyType,
            damageDice,
            null,
            damageType,
            weight,
            properties
        );
    }
    public WeaponItem(String name, int currencyAmount, CurrencyTypes currencyType, Dice damageDice, Dice altDamageDice, DamageTypes damageType, int weight, WeaponProperties... properties) {
        super(name, "wielding");

        this.properties = properties.length == 0 ? EnumSet.noneOf(WeaponProperties.class) : EnumSet.copyOf(Arrays.asList(properties));
        
        AttackRange atkRange = AttackRange.MELEE;
        if (this.properties.contains(WeaponProperties.REACH)) {
            atkRange = AttackRange.REACHING;
        } else if (this.properties.contains(WeaponProperties.RANGE)) {
            atkRange = AttackRange.RANGED;
        }
        this.range = atkRange;

        this.attack = new WeaponAttack(name, damageDice, damageType, atkRange);
        this.altAttack = (altDamageDice != null) 
            ? new WeaponAttack(name + " (2H)", altDamageDice, damageType, atkRange) 
            : null;

        this.weight = weight;
        this.currencyAmount = currencyAmount;
        this.currencyType = currencyType;

        this.isVersatile = this.properties.contains(WeaponProperties.VERSATILE);
        this.isHeavy = this.properties.contains(WeaponProperties.HEAVY);
        this.canOffHand = this.properties.contains(WeaponProperties.LIGHT);
        this.canMainHand = !this.isHeavy || this.isVersatile; 
        
        this.requiresAmmunition = this.properties.stream().anyMatch(p -> p.isAmmunition());
        this.canLoad = this.properties.contains(WeaponProperties.LOADING);
        this.isLoaded = false;
    }

    public boolean requiresBoth() { return isHeavy; }
    public boolean canOff() { return canOffHand; }
    public boolean canMain() { return canMainHand; }
    public boolean isVersatile() { return isVersatile; }
    public boolean canLoad() { return canLoad; }
    public boolean isLoaded() { return isLoaded; }
    public void unload() {  isLoaded = false; }
    public void reload() {  isLoaded = true; }

    public AttackAction getAttackAction(boolean currentlyTwoHanded) {
        if (canLoad) {
            return isLoaded ? attack : null;
        }
        if (currentlyTwoHanded && (requiresBoth() || isVersatile())) {
            return (altAttack != null) ? altAttack : attack;
        }
        return attack;
    }
    public AttackRange getRange() { return range; }
    public EnumSet<WeaponProperties> getProperties() { return properties; }
    public boolean hasProperty(WeaponProperties property) { return properties.contains(property); }

    public int getMagicBonus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMagicBonus'");
    }
}
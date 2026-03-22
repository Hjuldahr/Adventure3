package com.example.adventure.item;

import java.util.Arrays;
import java.util.EnumSet;

import com.example.adventure.action.WeaponAttack;

public abstract class WeaponTemplates {
    public enum WeaponProperties {
        LIGHT, 
        FINESSE, 
        THROWN, 
        TWO_HANDED, 
        VERSATILE, 
        LOADING, 
        HEAVY, 
        REACH, 
        SPECIAL, 
        AMMUNITION_BOLT, 
        AMMUNITION_ARROW, 
        AMMUNITION_STONE, 
        AMMUNITION_NEEDLE
    }
    public static final EnumSet<WeaponProperties> ammunitionProperties = EnumSet.of(
        WeaponProperties.AMMUNITION_BOLT, 
        WeaponProperties.AMMUNITION_ARROW, 
        WeaponProperties.AMMUNITION_STONE, 
        WeaponProperties.AMMUNITION_NEEDLE
    );
    
    public enum WeaponTypes {
        // SIMPLE MELEE
        CLUB("Club", 1, CurrencyTypes.SILVER, WeaponAttack.D4_BLUDGEONING, 2, WeaponProperties.LIGHT),
        DAGGER("Dagger", 2, CurrencyTypes.GOLD, WeaponAttack.D4_PIERCING, 1, WeaponProperties.FINESSE, WeaponProperties.LIGHT, WeaponProperties.THROWN),
        GREATCLUB("Greatclub", 2, CurrencyTypes.SILVER, WeaponAttack.D8_BLUDGEONING, 10, WeaponProperties.TWO_HANDED),
        HANDAXE("Handaxe", 5, CurrencyTypes.GOLD, WeaponAttack.D6_SLASHING, 2, WeaponProperties.LIGHT, WeaponProperties.THROWN),
        JAVELIN("Javelin", 5, CurrencyTypes.SILVER, WeaponAttack.D6_PIERCING, 2, WeaponProperties.THROWN),
        LIGHT_HAMMER("Light Hammer", 2, CurrencyTypes.GOLD, WeaponAttack.D4_BLUDGEONING, 2, WeaponProperties.LIGHT, WeaponProperties.THROWN),
        MACE("Mace", 5, CurrencyTypes.GOLD, WeaponAttack.D6_BLUDGEONING, 4),
        QUARTERSTAFF("Quarterstaff", 2, CurrencyTypes.SILVER, WeaponAttack.D6_BLUDGEONING, 4, WeaponProperties.VERSATILE),
        SICKLE("Sickle", 1, CurrencyTypes.GOLD, WeaponAttack.D4_SLASHING, 2, WeaponProperties.LIGHT),
        SPEAR("Spear", 1, CurrencyTypes.GOLD, WeaponAttack.D6_PIERCING, 3, WeaponProperties.THROWN, WeaponProperties.VERSATILE),

        // SIMPLE RANGED
        LIGHT_CROSSBOW("Light Crossbow", 25, CurrencyTypes.GOLD, WeaponAttack.D8_PIERCING, 5, WeaponProperties.AMMUNITION_BOLT, WeaponProperties.LOADING, WeaponProperties.TWO_HANDED),
        DART("Dart", 5, CurrencyTypes.COPPER, WeaponAttack.D4_PIERCING, 0, WeaponProperties.FINESSE, WeaponProperties.THROWN),
        SHORTBOW("Shortbow", 25, CurrencyTypes.GOLD, WeaponAttack.D6_PIERCING, 2, WeaponProperties.AMMUNITION_ARROW, WeaponProperties.TWO_HANDED),
        SLING("Sling", 1, CurrencyTypes.SILVER, WeaponAttack.D4_BLUDGEONING, 0, WeaponProperties.AMMUNITION_STONE),

        // MARTIAL MELEE
        BATTLEAXE("Battleaxe", 10, CurrencyTypes.GOLD, WeaponAttack.D8_SLASHING, 4, WeaponProperties.VERSATILE),
        FLAIL("Flail", 10, CurrencyTypes.GOLD, WeaponAttack.D8_BLUDGEONING, 2),
        GLAIVE("Glaive", 20, CurrencyTypes.GOLD, WeaponAttack.D10_SLASHING, 6, WeaponProperties.HEAVY, WeaponProperties.REACH, WeaponProperties.TWO_HANDED),
        GREATAXE("Greataxe", 30, CurrencyTypes.GOLD, WeaponAttack.D12_SLASHING, 7, WeaponProperties.HEAVY, WeaponProperties.TWO_HANDED),
        GREATSWORD("Greatsword", 50, CurrencyTypes.GOLD, WeaponAttack.D6_2_SLASHING, 6, WeaponProperties.HEAVY, WeaponProperties.TWO_HANDED),
        HALBERD("Halberd", 20, CurrencyTypes.GOLD, WeaponAttack.D10_SLASHING, 6, WeaponProperties.HEAVY, WeaponProperties.REACH, WeaponProperties.TWO_HANDED),
        LANCE("Lance", 10, CurrencyTypes.GOLD, WeaponAttack.D12_PIERCING, 6, WeaponProperties.REACH, WeaponProperties.SPECIAL),
        LONGSWORD("Longsword", 15, CurrencyTypes.GOLD, WeaponAttack.D8_SLASHING, 3, WeaponProperties.VERSATILE),
        MAUL("Maul", 10, CurrencyTypes.GOLD, WeaponAttack.D6_2_BLUDGEONING, 10, WeaponProperties.HEAVY, WeaponProperties.TWO_HANDED),
        MORNINGSTAR("Morningstar", 15, CurrencyTypes.GOLD, WeaponAttack.D8_PIERCING, 4),
        PIKE("Pike", 5, CurrencyTypes.GOLD, WeaponAttack.D10_PIERCING, 18, WeaponProperties.HEAVY, WeaponProperties.REACH, WeaponProperties.TWO_HANDED),
        RAPIER("Rapier", 25, CurrencyTypes.GOLD, WeaponAttack.D8_PIERCING, 2, WeaponProperties.FINESSE),
        SCIMITAR("Scimitar", 25, CurrencyTypes.GOLD, WeaponAttack.D6_PIERCING, 3, WeaponProperties.FINESSE, WeaponProperties.LIGHT),
        SHORTSWORD("Shortsword", 10, CurrencyTypes.GOLD, WeaponAttack.D6_PIERCING, 2, WeaponProperties.FINESSE, WeaponProperties.LIGHT),
        TRIDENT("Trident", 5, CurrencyTypes.GOLD, WeaponAttack.D6_PIERCING, 4, WeaponProperties.THROWN, WeaponProperties.VERSATILE),
        WAR_PICK("War Pick", 5, CurrencyTypes.GOLD, WeaponAttack.D8_PIERCING, 2),
        WARHAMMER("Warhammer", 15, CurrencyTypes.GOLD, WeaponAttack.D8_BLUDGEONING, 2, WeaponProperties.VERSATILE),
        WHIP("Whip", 2, CurrencyTypes.GOLD, WeaponAttack.D4_SLASHING, 3, WeaponProperties.FINESSE, WeaponProperties.REACH),

        // MARTIAL RANGED
        BLOWGUN("Blowgun", 10, CurrencyTypes.GOLD, WeaponAttack.F1_PIERCING, 1, WeaponProperties.AMMUNITION_NEEDLE, WeaponProperties.LOADING),
        HAND_CROSSBOW("Hand Crossbow", 75, CurrencyTypes.GOLD, WeaponAttack.D6_PIERCING, 3, WeaponProperties.AMMUNITION_BOLT, WeaponProperties.LIGHT, WeaponProperties.LOADING),
        HEAVY_CROSSBOW("Heavy Crossbow", 50, CurrencyTypes.GOLD, WeaponAttack.D10_PIERCING, 18, WeaponProperties.AMMUNITION_BOLT, WeaponProperties.HEAVY, WeaponProperties.LOADING, WeaponProperties.TWO_HANDED),
        LONGBOW("Longbow", 50, CurrencyTypes.GOLD, WeaponAttack.D8_PIERCING, 2, WeaponProperties.AMMUNITION_ARROW, WeaponProperties.HEAVY, WeaponProperties.TWO_HANDED),
        NET("Net", 1, CurrencyTypes.GOLD, WeaponAttack.NONE, 3, WeaponProperties.SPECIAL, WeaponProperties.THROWN);

        private final String name;
        private final int currencyAmount;
        private final CurrencyTypes currencyType;
        private final WeaponAttack weaponAttack;
        private final int weight;
        private final EnumSet<WeaponProperties> properties;

        WeaponTypes(String name, int currencyAmount, CurrencyTypes currencyType, WeaponAttack weaponAttack, int weight, WeaponProperties... properties) {
            this.name = name;
            this.currencyAmount = currencyAmount;
            this.currencyType = currencyType;
            this.weaponAttack = weaponAttack;
            this.weight = weight;
            this.properties = properties.length == 0 ? EnumSet.noneOf(WeaponProperties.class) : EnumSet.copyOf(Arrays.asList(properties));
        }

        public boolean hasProperty(WeaponProperties target) {
            return properties.contains(target); // Much faster and cleaner than a loop
        }

        public String getName() { return name; }
        public WeaponAttack getBaseAttack() { return weaponAttack; }
        public int getWeight() { return weight; }
        public EnumSet<WeaponProperties> getProperties() { return properties; }
        public boolean requiresAmmunition() { return !java.util.Collections.disjoint(properties, ammunitionProperties); }
    }
}
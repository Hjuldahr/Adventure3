package com.example.adventure.item;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.example.adventure.entity.PlayerEntity;
import com.example.adventure.item.WeaponItem.WeaponProperties;
import com.example.adventure.entity.Ability.AbilityTypes;
import com.example.adventure.utility.Colours;

public class Armoury {
    private static final int BASE_AC = 10;
    
    private WeaponItem equippedWeapon;
    private WeaponItem equippedOffWeapon;
    private Armour donnedArmour;
    private Shield equippedShield;

    private TreeSet<WeaponItem> weapons;
    private TreeSet<Armour> armours;
    private TreeSet<Shield> shields;
    
    public void viewStoredWeapon(PlayerEntity player) {
        ItemRarities previousCategory = null;

        for (WeaponItem weapon : weapons) {
            ItemRarities rarity = weapon.getRarity();
            
            // 1. Magic and Name
            int magicBonus = weapon.getMagicBonus();
            String magicBonusDisplay = (magicBonus > 0) ? " +" + magicBonus : "";
            String displayName = weapon.getName() + magicBonusDisplay;

            // 2. Costs
            String buyDisplay = (weapon.getBuyCost() <= 0) ? "*" : String.valueOf(weapon.getBuyCost());
            String sellDisplay = (weapon.getSellCost() <= 0) ? "*" : String.valueOf(weapon.getSellCost());
            String costText = buyDisplay + "/" + sellDisplay;

            // 3. Attack Bonus (To Hit)
            // Determine if Finesse allows Agility, otherwise default to Brawn
            AbilityTypes atkAbility = (weapon.hasProperty(WeaponProperties.FINESSE) && 
                                    player.getAbilityScore(AbilityTypes.AGILITY) > player.getAbilityScore(AbilityTypes.BRAWN)) 
                                    ? AbilityTypes.AGILITY : AbilityTypes.BRAWN;
            
            int atkMod = player.getAbilityModifier(atkAbility);
            int totalAtk = atkMod + player.getProfiencyBonus() + magicBonus;
            String atkDisplay = String.format("+%d", totalAtk);

            // 4. Damage Display (Handling Versatile)
            // 1H Damage: Dice + Mod + Magic
            String dmg1H = weapon.getAttackAction(false).toString() + " + " + (atkMod + magicBonus);
            
            // 2H Damage (if Versatile)
            String dmgDisplay = dmg1H;
            if (weapon.isVersatile() && weapon.getAttackAction(true) != null) {
                String dmg2H = weapon.getAttackAction(true).toString() + " + " + (atkMod + magicBonus);
                dmgDisplay = String.format("%s (1H) / %s (2H)", dmg1H, dmg2H);
            }

            // 5. Properties & Attunement
            String propertiesText = weapon.getProperties().isEmpty() ? "" : "\n\tProps: " + weapon.getProperties();
            String attuneText = (weapon.canAttune()) ? (player.attunementCheck(weapon) ? "\n\t[✦] Attunement" : "\n\t[ ] Attunement") : "";

            // 6. Print Header
            if (previousCategory == null || previousCategory != rarity) {
                previousCategory = rarity;
                System.out.printf("-=-=-=-: Weapons: %s%s%s :-=-=-=-\n", 
                    rarity.getColour(), rarity.getName(), Colours.RESET
                );
            }

            // 7. Print Detail
            System.out.printf("%s%s%s\n\tHit: %s | Dmg: %s [%s]\n\tCost: %s | Weight: %d lb%s%s\n", 
                rarity.getColour(), displayName, Colours.RESET, // Name
                atkDisplay, dmgDisplay, weapon.getDamageType(), // Offense
                costText, weapon.getWeight(),                   // Utility
                propertiesText, attuneText                      // Extras
            );
        }
    }

    public void viewStoredArmour(PlayerEntity player) {
        ItemRarities previousCategory = null;

        for (Armour armour : armours) {
            ItemRarities rarity = armour.getRarity();
            
            int magicBonus = armour.getMagicBonus();
            String magicBonusDisplay = (magicBonus > 0) ? " +" + String.valueOf(magicBonus) : "";

            String displayName = armour.getName() + magicBonusDisplay; 

            String buyDisplay = (armour.getBuyCost() <= 0) ? "*" : String.valueOf(armour.getBuyCost());
            String sellDisplay = (armour.getSellCost() <= 0) ? "*" : String.valueOf(armour.getSellCost());
            String costText = buyDisplay + "/" + sellDisplay;
            
            int baseAC = armour.getArmourClass();
            int agilityBonus = armour.getAddAgility() 
                ? Math.min(player.getAbilityModifier(AbilityTypes.AGILITY), armour.getMaxAgilityBonus()) 
                : 0;

            int totalAC = baseAC + agilityBonus + magicBonus;
            
            String formula = baseAC + (agilityBonus > 0 ? " + " + agilityBonus + " Ag" : "") 
                        + (magicBonus > 0 ? " + " + magicBonus : "");

            String acDisplay = (agilityBonus > 0 || magicBonus > 0) 
                ? String.format("%d (%s)", totalAC, formula) 
                : String.valueOf(baseAC);

            String brawnColor = (player.getAbilityScore(AbilityTypes.BRAWN) < armour.getMinBrawnScore()) ? Colours.RED.toString() : "";
            String brawnReqText = (armour.getMinBrawnScore() != 0) 
                ? "\n\t" + brawnColor + "Req Br " + armour.getMinBrawnScore() + "+" + Colours.RESET 
                : "";

            String attuneText = (armour.canAttune()) ? (player.attunementCheck(armour) ? "\n\t[✦] Attunement" : "\n\t[ ] Attunement") : "";

            if (previousCategory == null || previousCategory != rarity) {
                previousCategory = rarity;
                System.out.printf("-=-=-=-: Armour : %s%s%s :-=-=-=-\n", 
                    rarity.getColour(), rarity.getName(), Colours.RESET
                );
            }

            // Changed %d/%d to %s to match your costText string
            System.out.printf("%s%s%s\n\tAC: %s%s%s\n\tCost: %s\n\tWeight: %d lb\n", 
                rarity.getColour(), displayName, Colours.RESET, // Name Row (3 slots)
                acDisplay, brawnReqText, attuneText,             // AC/Req Row (3 slots)
                costText,                                       // Cost Row (1 slot)
                armour.getWeight()                              // Weight Row (1 slot)
            );
        }
    }
}

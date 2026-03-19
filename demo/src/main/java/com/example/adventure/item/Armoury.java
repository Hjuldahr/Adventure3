package com.example.adventure.item;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.example.adventure.entity.PlayerEntity;
import com.example.adventure.entity.AbilityScores.AbilityCategories;
import com.example.adventure.utility.Colours;

public class Armoury {
    private static final int BASE_AC = 10;
    
    private Weapon equippedWeapon;
    private Weapon equippedOffWeapon;
    private Armour donnedArmour;
    private Shield equippedShield;

    private TreeSet<Weapon> weapons;
    private TreeSet<Armour> armours;
    private TreeSet<Shield> shields;
    
    public void viewWeapon(PlayerEntity player) {
        ItemRarities previousCategory = null;

        










    }

    public void viewArmour(PlayerEntity player) {
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
                ? Math.min(player.getAbilityModifier(AbilityCategories.AGILITY), armour.getMaxAgilityBonus()) 
                : 0;

            int totalAC = baseAC + agilityBonus + magicBonus;
            
            String formula = baseAC + (agilityBonus > 0 ? " + " + agilityBonus + " Ag" : "") 
                        + (magicBonus > 0 ? " + " + magicBonus : "");

            String acDisplay = (agilityBonus > 0 || magicBonus > 0) 
                ? String.format("%d (%s)", totalAC, formula) 
                : String.valueOf(baseAC);

            String brawnColor = (player.getAbilityScore(AbilityCategories.BRAWN) < armour.getMinBrawnScore()) ? Colours.RED.toString() : "";
            String brawnReqText = (armour.getMinBrawnScore() != 0) 
                ? "\n\t" + brawnColor + "Req Br " + armour.getMinBrawnScore() + "+" + Colours.RESET 
                : "";

            String attuneText = (armour.canAttune()) ? (player.attunementCheck(armour) ? "\n\t[※] Attunement" : "\n\t[ ] Attunement") : "";

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

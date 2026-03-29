package com.example.adventure.creature;

import com.example.adventure.currency.CoinPurse;
import com.example.adventure.combat.AllegianceTypes;
import com.example.adventure.creature.Ability.AbilityTypes;
import com.example.adventure.creature.Proficiencies.ProficiencyTiers;
import com.example.adventure.item.Armour;
import com.example.adventure.item.Armoury;
import com.example.adventure.item.Inventory;
import com.example.adventure.item.Item;
import com.example.adventure.item.Shield;
import com.example.adventure.item.WeaponItem;
import com.example.adventure.randomizer.Dice;
import com.example.adventure.utility.RollEvaluator;
import com.example.adventure.utility.Success;
import com.example.adventure.utility.SuccessTypes;

public class PlayerCharacter extends Creature
{
    protected int playerLevel;
    protected PlayableRace playerRace;
    protected PlayerClass playerClass;
    
    private static final int BASE_AC = 10;
    private static final int DEATHSAVE_DC = 10;
    private static final int ENCUMBERANCE_MODIFIER = 10;
    private static final int REQUIRED_DEATHSAVES = 3;
    
    private Armoury armoury;
    private Inventory inventory;
    private CoinPurse coinPurse;

    private Item mainHand = null;
    private Item offHand = null;
    private Armour equippedArmour;

    private int passedDeathSaves = 0;
    private int failedDeathSaves = 0;
    private boolean performingDeathSaves = false;

    private SpellSlots spellSlots;

    public PlayerCharacter(
        int playerLevel,
        String name,
        PlayableRace playerRace,
        PlayerClass playerClass,
        Alignment alignment,
        Ability baseAbilities
    ) {
        super(
            AllegianceTypes.PARTY,

            name,
            playerRace.getSizeCategory(),
            CreatureType.HUMANOID,
            alignment,
            playerClass.getHitDiceForLevel(playerLevel),
            baseAbilities,
            new Proficiencies<AbilityTypes>(),
            new Proficiencies<SkillTypes>(),
            playerRace.getDamageAdjustments(),
            playerRace.getVisionTypes(),
            playerRace.getLanguages()
        );
        this.playerLevel = playerLevel;
        this.playerClass = playerClass;
        this.playerRace = playerRace;
    }

    public int getCarryLimit() {
        return abilities.getAbilityScore(AbilityTypes.BRAWN) * ENCUMBERANCE_MODIFIER;
    }

    @Override
    public int getArmourClass() {
        int baseAC = (equippedArmour != null) ? equippedArmour.getArmourClass() : BASE_AC;
        
        // If wearing armour, use the cap. If unarmoured, use full Agility modifier.
        int agilityBonus = (equippedArmour != null) 
            ? Math.min(equippedArmour.getMaxAgilityBonus(), abilities.getAbilityModifier(AbilityTypes.AGILITY)) 
            : abilities.getAbilityModifier(AbilityTypes.AGILITY);

        int armourClass = baseAC + agilityBonus;
        
        if (offHand instanceof Shield equippedShield) {
            armourClass += equippedShield.getArmourClass();
        }
        
        // Hint for your TODO: You could add a 'magicBonus' variable here 
        // to sum up rings of protection, mage armour, etc.

        return armourClass;
    }

    private void performDeathSave() {
        int raw = Dice.D20();
        SuccessTypes successType = RollEvaluator.evaluate(raw, raw, DEATHSAVE_DC);

        switch (successType) {
            case CRIT_SUCCESS -> { passedDeathSaves += 2; }:
            case SUCCESS -> { passedDeathSaves++; }:
            case FAILURE -> { failedDeathSaves++; }:
            case CRIT_FAILURE -> { failedDeathSaves += 2; }:
        }

        if (failedDeathSaves >= REQUIRED_DEATHSAVES) {
            //TODO trigger game over
        } else if (passedDeathSaves >= REQUIRED_DEATHSAVES) {
            passedDeathSaves = 0;
            failedDeathSaves = 0;
            // slightly more generous due to being singleplayer + NPCs
            hitpoints.setValue(1); 
            performingDeathSaves = false;
        }
    }

    @Override
    public void turn() {
        if (performingDeathSaves) {
            performDeathSave();
            if (hitPoints.atMinimum()) return;
        }

        super.turn();
    }

    // Item Logic

    public Inventory getInventory() {
        return inventory;
    }

    public void viewInventory() {
        // Check if hands are empty first
        if (mainHand == null && offHand == null) {
            System.out.println("Your hands are empty.");
        }
        // Handle two-handed items
        else if (mainHand != null && offHand == null && mainHand.requiresBoth()) {
            System.out.printf("You are %s a %s with both-hands.\n", mainHand.getVerb(), mainHand.getName());
        }
        // Handle dual-wielding (both hands full)
        else if (mainHand != null && offHand != null) {
            // Special case: Identical items (e.g., two "Iron Daggers")
            if (mainHand.getName().equals(offHand.getName())) {
                System.out.printf("You are dual-wielding %ss.\n", mainHand.getName());
            } 
            else {
                System.out.printf("You are %s a %s and %s a %s.\n", 
                    mainHand.getVerb(), mainHand.getName(), offHand.getVerb(), offHand.getName());
            }
        } 
        // Single hand cases
        else if (mainHand != null) {
            System.out.printf("You are %s a %s with your main-hand.\n", mainHand.getVerb(), mainHand.getName());
        } 
        else {
            System.out.printf("You are %s a %s with your off-hand.\n", offHand.getVerb(), offHand.getName());
        }

        inventory.viewInventory(getCarryLimit());
    }

    // Holding Logic
    // Returns item incase you are dropping or trading instead of stowing / weapon swapping

    public Item getMainHand() {
        return mainHand;
    }
    public Item getOffhand() {
        return offHand;
    }
    public void holdItem(Item newItem) {
        if (newItem == null) return;

        if (newItem instanceof WeaponItem weaponItem && mainHand instanceof WeaponItem mainWeaponItem) {
            // 1. HEAVY: Occupies both hands physically.
            if (weaponItem  .requiresBoth()) {
                stowItem(mainHand);
                stowItem(offHand);
                mainHand = weaponItem;
                offHand = null;
                return;
            }

            // 2. SHIELD: Strictly off-hand.
            if (weaponItem.canOff() && !weaponItem.canMain()) {
                // If holding a Heavy weapon, it must be stowed.
                if (mainHand != null && mainWeaponItem.requiresBoth()) {
                    stowItem(mainHand);
                    mainHand = null;
                }
                stowItem(offHand);
                offHand = weaponItem;
                return;
            }

            // 3. MAIN-HAND CAPABLE (Light, Other, Versatile)
            if (weaponItem.canMain()) {
                if (mainHand != null) {
                    // SHIFT: Only Light/Other (canOff) can move to the off-hand.
                    // Versatile (canMain but !canOff) will be stowed instead.
                    if (offHand == null && mainWeaponItem.canOff()) {
                        offHand = mainHand;
                    } else {
                        stowItem(mainHand);
                    }
                }
                mainHand = weaponItem;
            }
        }
    }

    public boolean isTwoHanding() {
        if (mainHand == null) return false;
        // Heavy forces two hands. Versatile uses both ONLY if off-hand is empty.
        return mainHand.requiresBoth() || (mainHand.isVersatile() && offHand == null);
    }

    public boolean canCastSpell() {
        // Rule: Requires at least one hand free.
        // 1. If either hand is physically empty, you can cast.
        if (mainHand == null || offHand == null) {
            // Exception: Heavy weapons physically occupy both hands even if offHand is null.
            if (mainHand != null && mainHand.requiresBoth()) return false;
            // Versatile weapons are assumed to swap stances, so having no item 
            // in offHand counts as a free hand for casting.
            return true; 
        }
        // 2. If both hands have items (e.g., Sword + Shield or Dual Wielding), no hand is free.
        return false;
    }

    private void stowItem(Item item) {
        if (item != null) {
            inventory.add(item);
        }
    }

    public boolean attunementCheck(Armour armour) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'attunementCheck'");
    }

    public boolean attunementCheck(WeaponItem weapon) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'attunementCheck'");
    }

    @Override
    public void takeDamage(int damage, DamageTypes damageType, boolean wasCritical, Creature attacker) {
        super.takeDamage(damage, damageType, wasCritical, attacker);

        if (performingDeathSaves) {
            failedDeathSaves += isCritical ? 2 : 1;
        } else {
            performingDeathSaves = hitpoints.atMinimum(); // 0 hitpoints
        }
    }

    @Override
    public void receiveHealing(int healing, boolean isCritical) {
        super.receiveHealing(healing, isCritical);

        if (performingDeathSaves && !hitpPoints.atMinimum()) {
            passedDeathSaves = 0;
        } else {
            performingDeathSaves = hitPoints.atMinimum();
        }
    }

    public void viewSkills() {
        System.out.printf("%+d Proficiency Bonus\n", profiencyBonus);
        
        for (AbilityTypes abilityType : AbilityTypes.values()) {
            int score = getAbilityScore(abilityType); 
            int mod = getAbilityModifier(abilityType);
            
            System.out.printf("%s %d (%+d)\n", 
                abilityType.getName(), score, mod);

            ProficiencyTiers saveTier = saveProficiencies.getProficiencyTier(abilityType);
            System.out.printf("\t[%s] %+d Saving Throw\n", 
                saveTier.getSymbol(), getSaveModifier(abilityType));

            for (SkillTypes skill : SkillTypes.getSkillsByAbility(abilityType)) {
                ProficiencyTiers skillTier = skillProficiencies.getProficiencyTier(skill);
                System.out.printf("\t[%s] %+d %s\n", 
                    skillTier.getSymbol(), getSkillModifier(skill), skill.getName());
            }
        }
    }

    
}
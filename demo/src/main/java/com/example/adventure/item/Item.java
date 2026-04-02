package com.example.adventure.item;

import com.example.adventure.creature.Alignment;
import com.example.adventure.creature.PlayableRace;
import com.example.adventure.creature.PlayerCharacter;
import com.example.adventure.creature.PlayerClass;
import com.example.adventure.creature.Ability.AbilityTypes;
import com.example.adventure.creature.Alignment.SubAlignments;
import com.example.adventure.utility.Colours;

public class Item implements Comparable<Item> {
    protected ItemRarities rarity;
    protected String name;
    protected int buyCost;
    protected int sellCost;
    protected int weight;
    protected String verb; // wielding / holding / bearing / etc
    protected int magicBonus;
    protected boolean isIndisposable;
    protected boolean isAttunable;

    protected AttunementRestrictionType attunementRestrictionType;
    protected Object attunementRestriction;

    protected Item(String name, String verb) {
        this.name = name;
        this.verb = verb;
    }

    protected Item(String name) {
        this(name, "holding");
    }

    public String toString() {
        return String.format("%s[%-9s]%s %s\n\tCost: %d/%d\n\tWeight: %d", 
            rarity.getColour(), 
            rarity.getName(), 
            Colours.RESET,
            name, 
            buyCost, sellCost,
            weight
        );
    }

    protected String getName() {
        return name;
    }

    protected String getVerb() {
        return verb;
    }

    protected ItemRarities getRarity() {
        return rarity;
    }

    public int compareTo(Item other) {
        int difference = this.rarity.compareTo(other.rarity);
        if (difference != 0) return difference;
        return this.name.compareToIgnoreCase(other.name);
    }

    public int getBuyCost() {
        return buyCost;
    }
    public int getSellCost() {
        return sellCost;
    }
    public int getWeight() {
        return weight;
    }
    public boolean isIndisposable() {
        return isIndisposable;
    }
    // PlayerCharacter enforces max attune limit (allowing for rare over-attuning 4/3)
    public boolean canAttune(PlayerCharacter playerCharacter) {
        return switch (attunementRestrictionType) {
            case PLAYER_CLASS -> {
                if (attunementRestriction instanceof PlayerClass playerClass) {
                    yield playerCharacter.getPlayerClass().equals(playerClass);
                }
                yield false;
            }
            case PLAYABLE_RACE -> {
                if (attunementRestriction instanceof PlayableRace playableRace) {
                    yield playerCharacter.getPlayerRace().equals(playableRace);
                }
                yield false;
            }
            case ALIGNMENT -> {
                if (attunementRestriction instanceof SubAlignments alignment) {
                    yield playerCharacter.getAlignment().hasSubAlignment(alignment);
                }
                yield false;
            }
            case NON_ALIGNMENT -> {
                if (attunementRestriction instanceof SubAlignments alignment) {
                    yield !playerCharacter.getAlignment().hasSubAlignment(alignment);
                }
                yield false;
            }
            case ABILITY_TYPE -> {
                //TODO
                yield false;
            }
            case PROFICIENCY -> {
                //TODO
                yield false;
            }
            case ANY -> true;
            case NONE -> false; // cannot attune if non-attunable
        };
    }
    public boolean isMagical() {
        return 
    }
    public int getMagicBonus() {
        return magicBonus;
    }
}
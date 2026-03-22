package com.example.adventure.entity;

import java.util.EnumMap;
import java.util.EnumSet;

import com.example.adventure.entity.Ability.AbilityTypes;

public enum SkillTypes {
    ATHLETICS("Athletics", AbilityTypes.BRAWN),
    GRAPPLING("Grappling", AbilityTypes.BRAWN),
    
    ENDURANCE("Endurance", AbilityTypes.FORTITUDE),
    TOLERANCE("Tolerance", AbilityTypes.FORTITUDE),
    
    ACROBATICS("Acrobatics", AbilityTypes.AGILITY),
    SLEIGHT_OF_HAND("Sleight of Hand", AbilityTypes.AGILITY),
    STEALTH("Stealth", AbilityTypes.AGILITY),

    LORE("Lore", AbilityTypes.INTELLECT),
    INVESTIGATION("Investigation", AbilityTypes.INTELLECT),
    PERCEPTION("Perception", AbilityTypes.INTELLECT),
    MEDICINE("Medicine", AbilityTypes.INTELLECT),
    SURVIVAL("Survival", AbilityTypes.INTELLECT),
    
    RELIGION("Religion", AbilityTypes.SPIRIT),
    ARCANA("Arcana", AbilityTypes.SPIRIT),
    INSIGHT("Insight", AbilityTypes.SPIRIT),
    NATURE("Nature", AbilityTypes.SPIRIT),

    ANIMAL_HANDLING("Animal Handling", AbilityTypes.CHARM),
    DECEPTION("Deception", AbilityTypes.CHARM),
    PERSUASION("Persuasion", AbilityTypes.CHARM),
    INTIMIDATION("Intimidation", AbilityTypes.CHARM),
    PERFORMANCE("Performance", AbilityTypes.CHARM);

    private final String name;
    private AbilityTypes abilityType;

    private static EnumMap<AbilityTypes,EnumSet<SkillTypes>> skillAssociationReverseLookup;
    static {
        skillAssociationReverseLookup = new EnumMap<>(AbilityTypes.class);
        for (SkillTypes skill : values()) {
            skillAssociationReverseLookup
                .computeIfAbsent(skill.getAbilityType(), k -> EnumSet.noneOf(SkillTypes.class))
                .add(skill);
        }
    }

    SkillTypes(String name, AbilityTypes abilityType) {
        this.name = name;
        this.abilityType = abilityType;
    }

    public String getName() { return name; }
    public AbilityTypes getAbilityType() { return abilityType; }

    public static EnumSet<SkillTypes> getSkillsByAbility(AbilityTypes abilityType) {
        return skillAssociationReverseLookup.get(abilityType);
    }
}
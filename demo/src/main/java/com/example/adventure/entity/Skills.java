package com.example.adventure.entity;

import com.example.adventure.entity.Ability.AbilityTypes;

public class Skills {
    public static enum SkillTypes {
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

        SkillTypes(String name, AbilityTypes abilityType) {
            this.name = name;
            this.abilityType = abilityType;
        }

        public String getName() { return name; }
        public AbilityTypes getAbilityType() { return abilityType; }
    };
}

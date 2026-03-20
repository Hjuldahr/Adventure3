package com.example.adventure.entity;

import java.util.EnumMap;

import com.example.adventure.entity.Ability.AbilityTypes;
import com.example.adventure.entity.Skills.SkillTypes;
import com.example.adventure.item.ArmourTypes;
import com.example.adventure.item.ToolTypes;
import com.example.adventure.item.WeaponTypes;

public class Proficiencies {
    public enum ProficiencyCategory {
        NONE(" ", "", 0), 
        PROFICIENCY("○", "Proficiency", 1), 
        EXPERTISE("◎", "Expertise", 2);

        private final String symbol;
        private final String name;
        private final int multiplier;

        ProficiencyCategory(String symbol, String name, int multiplier) {
            this.symbol = symbol;
            this.name = name;
            this.multiplier = multiplier;
        }

        public String getSymbol() { return symbol; }
        public String getName() { return name; }
        public int getMod() { return multiplier; }
    };

    private EnumMap<ArmourTypes,ProficiencyCategory> armourProficiencies;
    private EnumMap<WeaponTypes,ProficiencyCategory> weaponProficiencies;
    private EnumMap<ToolTypes,ProficiencyCategory> toolProficiencies;
    private EnumMap<SkillTypes,ProficiencyCategory> skillProficiencies;
    private EnumMap<AbilityTypes,ProficiencyCategory> saveProficiencies;

    public Proficiencies () {
        this(null, null, null, null, null);
    }
    public Proficiencies (
        EnumMap<ArmourTypes,ProficiencyCategory> armourProficiencies,
        EnumMap<AbilityTypes,ProficiencyCategory> saveProficiencies,
        EnumMap<SkillTypes,ProficiencyCategory> skillProficiencies,
        EnumMap<ToolTypes,ProficiencyCategory> toolProficiencies,
        EnumMap<WeaponTypes,ProficiencyCategory> weaponProficiencies
    ) {
        this.armourProficiencies = (armourProficiencies == null) 
            ? new EnumMap<>(ArmourTypes.class) 
            : new EnumMap<>(armourProficiencies);
        this.weaponProficiencies = (weaponProficiencies == null) 
            ? new EnumMap<>(WeaponTypes.class) 
            : new EnumMap<>(weaponProficiencies);
        this.toolProficiencies = (toolProficiencies == null) 
            ? new EnumMap<>(ToolTypes.class) 
            : new EnumMap<>(toolProficiencies);
        this.skillProficiencies = (skillProficiencies == null) 
            ? new EnumMap<>(SkillTypes.class) 
            : new EnumMap<>(skillProficiencies);
        this.saveProficiencies = (saveProficiencies == null) 
            ? new EnumMap<>(AbilityTypes.class) 
            : new EnumMap<>(saveProficiencies);
    }
    public Proficiencies (Proficiencies other) {
        this(other.armourProficiencies, 
            other.saveProficiencies, 
            other.skillProficiencies, 
            other.toolProficiencies,
            other.weaponProficiencies
        );
    }

    public boolean isProficient(ArmourTypes type) {
        return armourProficiencies.getOrDefault(type, ProficiencyCategory.NONE) != ProficiencyCategory.NONE;
    }
    public int calculateProficiencyBonus(WeaponTypes type, int proficiencyBonus) {
        ProficiencyCategory category = weaponProficiencies.getOrDefault(type, ProficiencyCategory.NONE);
        return proficiencyBonus * category.getMod();
    }
    public int calculateProficiencyBonus(ToolTypes type, int proficiencyBonus) {
        ProficiencyCategory category = toolProficiencies.getOrDefault(type, ProficiencyCategory.NONE);
        return proficiencyBonus * category.getMod();
    }
    public int calculateProficiencyBonus(SkillTypes type, int proficiencyBonus) {
        ProficiencyCategory category = skillProficiencies.getOrDefault(type, ProficiencyCategory.NONE);
        return proficiencyBonus * category.getMod();
    }
    public int calculateProficiencyBonus(AbilityTypes type, int proficiencyBonus) {
        ProficiencyCategory category = saveProficiencies.getOrDefault(type, ProficiencyCategory.NONE);
        return proficiencyBonus * category.getMod();
    }

    public void setProficiencyCategory(ArmourTypes type, ProficiencyCategory category) {
        armourProficiencies.put(type, category);
    }
    public void setProficiencyCategory(WeaponTypes type, ProficiencyCategory category) {
        weaponProficiencies.put(type, category);
    }
    public void setProficiencyCategory(ToolTypes type, ProficiencyCategory category) {
        toolProficiencies.put(type, category);
    }
    public void setProficiencyCategory(SkillTypes type, ProficiencyCategory category) {
        skillProficiencies.put(type, category);
    }
    public void setProficiencyCategory(AbilityTypes type, ProficiencyCategory category) {
        saveProficiencies.put(type, category);
    }

    public ProficiencyCategory getProficiencyCategory(ArmourTypes type) {
        return armourProficiencies.getOrDefault(type, ProficiencyCategory.NONE);
    }
    public ProficiencyCategory getProficiencyCategory(WeaponTypes type) {
        return weaponProficiencies.getOrDefault(type, ProficiencyCategory.NONE);
    }
    public ProficiencyCategory getProficiencyCategory(ToolTypes type) {
        return toolProficiencies.getOrDefault(type, ProficiencyCategory.NONE);
    }
    public ProficiencyCategory getProficiencyCategory(SkillTypes type) {
        return skillProficiencies.getOrDefault(type, ProficiencyCategory.NONE);
    }
    public ProficiencyCategory getProficiencyCategory(AbilityTypes type) {
        return saveProficiencies.getOrDefault(type, ProficiencyCategory.NONE);
    }
}

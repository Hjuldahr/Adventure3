package com.example.adventure.creature;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.example.adventure.creature.Proficiencies.ProficiencyTiers;

public class Ability {
    public static final int MAXIMUM = 30;
    public static final int MINIMUM = 0;
    public static final int BASELINE = 10;

    public static enum AbilityTypes {
        BRAWN("BR", "Brawn"),
        FORTITUDE("FO", "Fortitude"),
        AGILITY("AG", "Agility"),
        INTELLECT("IN", "Intellect"),
        CHARM("CH", "Charm"),
        SPIRIT("SP", "Spirit");

        private final String symbol;
        private final String name;

        AbilityTypes(String symbol, String name) {
            this.symbol = symbol;
            this.name = name;
        }

        public String getSymbol() { return symbol; }
        public String getName() { return name; }
    };

    public static HashMap<Integer,Integer> abilityModifierLookup = new HashMap<>();

    private EnumMap<AbilityTypes,Integer> abilities;

    public Ability(int br, int fo, int ag, int in, int ch, int sp) {
        abilities = new EnumMap<>(AbilityTypes.class);
        abilities.put(AbilityTypes.BRAWN, br);
        abilities.put(AbilityTypes.FORTITUDE, fo);
        abilities.put(AbilityTypes.AGILITY, ag);
        abilities.put(AbilityTypes.INTELLECT, in);
        abilities.put(AbilityTypes.CHARM, ch);
        abilities.put(AbilityTypes.SPIRIT, sp);
    }

    public Ability(Ability other) {
        abilities = new EnumMap<>(other.abilities);
    }

    public int getAbilityScore(AbilityTypes category) {
        return abilities.getOrDefault(category, BASELINE);
    }

    public void setAbilityScore(AbilityTypes category, int value) {
        abilities.put(category, constrainValue(value));
    }

    public void incrementAbilityScore(AbilityTypes category) {
        increaseAbilityScore(category, 1);
    }

    public void increaseAbilityScore(AbilityTypes category, int increase) {
        abilities.merge(category, increase, (value, change) -> 
            constrainValue(value + change)
        );
    }

    public void decrementAbilityScore(AbilityTypes category) {
        decreaseAbilityScore(category, 1);
    }

    public void decreaseAbilityScore(AbilityTypes category, int decrease) {
        abilities.merge(category, decrease, (value, change) -> 
            constrainValue(value - change)
        );
    }

    private int constrainValue(int value) {
        return Math.clamp(value, MINIMUM, MAXIMUM);
    }

    public int getAbilityModifier(AbilityTypes category) {
        int score = abilities.getOrDefault(category, BASELINE);
        return abilityModifierLookup.computeIfAbsent(score, s -> Math.floorDiv(s - BASELINE, 2));
    }

    public void viewAbilities(Proficiencies<AbilityTypes> proficiencies) {
        for (Map.Entry<AbilityTypes,Integer> ability : abilities.entrySet()) {
            AbilityTypes abilityType = ability.getKey();
            int abilityScore = ability.getValue();
            ProficiencyTiers proficiencyTier = proficiencies.getProficiencyTier(abilityType);

            int abilityMod = abilityModifierLookup.get(abilityScore);
            String abilityModText = abilityMod == 0 ? "" : (abilityMod > 0 ? "+" + abilityMod : Integer.toString(abilityMod));

            System.out.printf("[%s] %s %s %s\n", 
                proficiencyTier.getSymbol(), abilityScore, abilityModText
            );
        }
    }
}
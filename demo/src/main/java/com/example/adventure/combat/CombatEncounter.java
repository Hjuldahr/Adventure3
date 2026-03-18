package com.example.adventure.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.adventure.entity.Entity;
import com.example.adventure.entity.AbilityScores.AbilityCategories;
import com.example.adventure.entity.AllegianceCategories;
import com.example.adventure.utility.CyclicList;
import com.example.adventure.utility.Dice;

public class CombatEncounter 
{
    CyclicList<Entity> combatOrder = new CyclicList<>();

    public CombatEncounter() {
        
        
    }

    private void setCombatants(List<Entity> combatants) {
        combatOrder = new CyclicList<>(combatants.size());
        List<Entry> entries = new ArrayList<>();

        for (Entity combatant : combatants) {
            for (int n = 0; n < combatant.getOnitiativeCount(); n++) {
                entries.add(new Entry(rollInitiative(combatant), combatant));
            }
        }
        entries.sort(Entry::compareTo);

        int targetId = 1;
        HashMap<Entity,Integer> entityIds = new HashMap<>(); // to track uniqueness

        for (Entry entry : entries) {
            Entity combatant = entry.entity();

            if (!entityIds.containsKey(combatant)) {
                // 1. Map the entity to the current ID
                entityIds.put(combatant, targetId);
                // 2. Add to the actual game combat order
                combatOrder.putNext(targetId, combatant);
                // 3. Increment for the NEXT unique entity
                targetId++;
            } else {
                // This is a multi-initiative turn; reuse the ID
                int existingId = entityIds.get(combatant);
                combatOrder.putNext(existingId, combatant);
            }
        }
    }

    private record Entry(int initiativeRoll, Entity entity) {
        public int compareTo(Entry other) {
            // 1. Primary: Initiative Roll (Descending)
            if (this.initiativeRoll != other.initiativeRoll) {
                return Integer.compare(other.initiativeRoll, this.initiativeRoll);
            }

            // 2. Tiebreaker: Ability Score (Descending)
            int thisAgility = entity.getAbilityScore(AbilityCategories.AGILITY);
            int otherAgility = other.entity.getAbilityScore(AbilityCategories.AGILITY);
            if (thisAgility != otherAgility) {
                return Integer.compare(otherAgility, thisAgility);
            }

            // 3. Final Fallback: Prevents "random" shuffling if both are equal
            // You could use entity name, a unique ID, or a coin flip
            return this.entity.getName().compareTo(other.entity.getName());
        }
    }

    private Integer rollInitiative(Entity entity) {
        int raw = Dice.d20();

        if (entity.hasInitiativeAdvantage()) {
            raw = Math.max(raw, Dice.d20());
        }

        int result = raw + entity.getInitiativeModifier();
        return result;
    }

    // after set combatants, etc
    public CombatState initiateCombat() {
        int roundNumber = 1;

        if (!combatCheck()) return CombatState.NO_COMBAT; 

        combatOrder.forEach(Entity::startOfEncounter);

        while (nextRound(roundNumber++)) {}

        combatOrder.forEach(Entity::endOfEncounter);

        return combatStateCheck();
    }

    private boolean nextRound(int roundNumber) {
        combatOrder.forEach(Entity::startOfRound);

        while (combatOrder.hasNext()) {
            nextTurn(roundNumber);
            if (!combatCheck()) return false; // end combat
        }
        combatOrder.select(0);

        combatOrder.forEach(Entity::endOfRound);
        return true; // continue combat
    }

    private void nextTurn(int roundNumber) {
        Entity combatant = combatOrder.next();

        combatant.startOfTurn();
        combatant.turn();
        combatant.endOfTurn();

        // check against end of turn DoT damage
        if (combatant.isDefeated() && !combatant.getPersistOnDeath()) {
            combatOrder.remove(combatant);
        }
    }

    private boolean combatCheck() {
        // still in combat?
        return combatStateCheck() == CombatState.IN_COMBAT;
    }

    private CombatState combatStateCheck() {
        // categorize combat state
        boolean anyAllies = combatOrder.elements().stream()
            .anyMatch(e -> e.getAllegiance() == AllegianceCategories.FRIENDLY && !e.isDefeated());

        if (!anyAllies) {
            return CombatState.PARTY_DEFEAT;
        }
        
        boolean anyEnemies = combatOrder.elements().stream()
            .anyMatch(e -> e.getAllegiance() == AllegianceCategories.HOSTILE && !e.isDefeated());

        if (!anyEnemies) {
            return CombatState.PARTY_VICTORY;
        }

        return CombatState.IN_COMBAT;
    }
}

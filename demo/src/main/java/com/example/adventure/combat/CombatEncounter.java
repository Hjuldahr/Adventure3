package com.example.adventure.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.adventure.entity.Entity;
import com.example.adventure.entity.NonPlayerEntity;
import com.example.adventure.entity.Ability.AbilityTypes;
import com.example.adventure.entity.EnemyEntity;
import com.example.adventure.utility.CyclicList;
import com.example.adventure.utility.Dice;

public class CombatEncounter 
{
    private CyclicList<Entity> combatOrder = new CyclicList<>();

    // true is front line, false is back line
    private HashMap<Entity,Boolean> party;
    private HashMap<Entity,Boolean> enemies;

    public CombatEncounter() {
        
        
    }

    // test if party member is in melee range of target
    public boolean isMeleeRange(Entity attacker, Entity target, boolean hasReach) {
        Boolean attackerFront = getPosition(attacker);
        Boolean targetFront = getPosition(target);

        if (attackerFront == null || targetFront == null) return false;
        
        if (!hasReach) {
            return attackerFront && targetFront; // both upfront
        } else {
            return attackerFront || targetFront; // atleast one upfront
        }
    }

    public boolean isRangedRange(Entity attacker, Entity target) {
        Boolean attackerFront = getPosition(attacker);
        Boolean targetFront = getPosition(target);

        if (attackerFront == null || targetFront == null) return false;

        return !attackerFront || !targetFront; // atleast one not upfront
    }

    // Helper to check which map the entity is in
    public Boolean getPosition(Entity e) {
        if (party.containsKey(e)) return party.get(e);
        if (enemies.containsKey(e)) return enemies.get(e);
        return null;
    }

    public void setCombatants(List<Entity> combatants) {
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
            int thisAgility = entity.getAbilityScore(AbilityTypes.AGILITY);
            int otherAgility = other.entity.getAbilityScore(AbilityTypes.AGILITY);
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

        combatOrder.forEach(e -> e.startOfEncounter(this));

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
            .anyMatch(e -> e.getAllegiance() == AllegianceTypes.PARTY && !e.isDefeated());

        if (!anyAllies) {
            return CombatState.PARTY_DEFEAT;
        }
        
        boolean anyEnemies = combatOrder.elements().stream()
            .anyMatch(e -> e.getAllegiance() == AllegianceTypes.ENEMY && !e.isDefeated());

        if (!anyEnemies) {
            return CombatState.PARTY_VICTORY;
        }

        return CombatState.IN_COMBAT;
    }

    public Set<Entity> getEnemies() {
        return enemies.keySet();
    }

    public Set<Entity> getParty() {
        return party.keySet();
    }

    public void setPosition(Entity e, boolean b) {
        if (party.containsKey(e)) {
            party.put(e, b);
        }
        else if (enemies.containsKey(e)) {
            enemies.put(e, b);
        }
    }
}

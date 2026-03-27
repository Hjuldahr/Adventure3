package com.example.adventure.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.adventure.creature.Creature;
import com.example.adventure.creature.NonPlayerCreature;
import com.example.adventure.creature.Ability.AbilityTypes;
import com.example.adventure.creature.EnemyCreature;
import com.example.adventure.utility.CyclicList;
import com.example.adventure.utility.Dice;

public class CombatEncounter 
{
    private CyclicList<Creature> combatOrder = new CyclicList<>();

    // true is front line, false is back line
    private HashMap<Creature,Boolean> party;
    private HashMap<Creature,Boolean> enemies;

    public CombatEncounter() {
        
        
    }

    // test if party member is in melee range of target
    public boolean isMeleeRange(Creature attacker, Creature target, boolean hasReach) {
        Boolean attackerFront = getPosition(attacker);
        Boolean targetFront = getPosition(target);

        if (attackerFront == null || targetFront == null) return false;
        
        if (!hasReach) {
            return attackerFront && targetFront; // both upfront
        } else {
            return attackerFront || targetFront; // atleast one upfront
        }
    }

    public boolean isRangedRange(Creature attacker, Creature target) {
        Boolean attackerFront = getPosition(attacker);
        Boolean targetFront = getPosition(target);

        if (attackerFront == null || targetFront == null) return false;

        return !attackerFront || !targetFront; // atleast one not upfront
    }

    // Helper to check which map the creature is in
    public Boolean getPosition(Creature e) {
        if (party.containsKey(e)) return party.get(e);
        if (enemies.containsKey(e)) return enemies.get(e);
        return null;
    }

    public void setCombatants(List<Creature> combatants) {
        combatOrder = new CyclicList<>(combatants.size());
        List<Entry> entries = new ArrayList<>();

        for (Creature combatant : combatants) {
            for (int n = 0; n < combatant.getOnitiativeCount(); n++) {
                entries.add(new Entry(rollInitiative(combatant), combatant));
            }
        }
        entries.sort(Entry::compareTo);

        int targetId = 1;
        HashMap<Creature,Integer> creatureIds = new HashMap<>(); // to track uniqueness

        for (Entry entry : entries) {
            Creature combatant = entry.creature();

            if (!creatureIds.containsKey(combatant)) {
                // 1. Map the creature to the current ID
                creatureIds.put(combatant, targetId);
                // 2. Add to the actual game combat order
                combatOrder.putNext(targetId, combatant);
                // 3. Increment for the NEXT unique creature
                targetId++;
            } else {
                // This is a multi-initiative turn; reuse the ID
                int existingId = creatureIds.get(combatant);
                combatOrder.putNext(existingId, combatant);
            }
        }
    }

    private record Entry(int initiativeRoll, Creature creature) {
        public int compareTo(Entry other) {
            // 1. Primary: Initiative Roll (Descending)
            if (this.initiativeRoll != other.initiativeRoll) {
                return Integer.compare(other.initiativeRoll, this.initiativeRoll);
            }

            // 2. Tiebreaker: Ability Score (Descending)
            int thisAgility = creature.getAbilityScore(AbilityTypes.AGILITY);
            int otherAgility = other.creature.getAbilityScore(AbilityTypes.AGILITY);
            if (thisAgility != otherAgility) {
                return Integer.compare(otherAgility, thisAgility);
            }

            // 3. Final Fallback: Prevents "random" shuffling if both are equal
            // You could use creature name, a unique ID, or a coin flip
            return this.creature.getName().compareTo(other.creature.getName());
        }
    }

    private Integer rollInitiative(Creature creature) {
        int raw = Dice.d20();

        if (creature.hasInitiativeAdvantage()) {
            raw = Math.max(raw, Dice.d20());
        }

        int result = raw + creature.getInitiativeModifier();
        return result;
    }

    // after set combatants, etc
    public CombatState initiateCombat() {
        int roundNumber = 1;

        if (!combatCheck()) return CombatState.NO_COMBAT; 

        combatOrder.forEach(e -> e.startOfEncounter(this));

        while (nextRound(roundNumber++)) {}

        combatOrder.forEach(Creature::endOfEncounter);

        return combatStateCheck();
    }

    private boolean nextRound(int roundNumber) {
        combatOrder.forEach(Creature::startOfRound);

        while (combatOrder.hasNext()) {
            nextTurn(roundNumber);
            if (!combatCheck()) return false; // end combat
        }
        combatOrder.select(0);

        combatOrder.forEach(Creature::endOfRound);
        return true; // continue combat
    }

    private void nextTurn(int roundNumber) {
        Creature combatant = combatOrder.next();

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

    public Set<Creature> getEnemies() {
        return enemies.keySet();
    }

    public Set<Creature> getParty() {
        return party.keySet();
    }

    public void setPosition(Creature e, boolean b) {
        if (party.containsKey(e)) {
            party.put(e, b);
        }
        else if (enemies.containsKey(e)) {
            enemies.put(e, b);
        }
    }
}

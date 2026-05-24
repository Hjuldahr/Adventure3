package com.example.adventure.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.adventure.entities.Entity;

public class CombatEncounter {
    private CombatContext combatContext;

    public CombatEncounter(List<Entity> entities) {
        List<Entity> temp = new ArrayList<>(entities);

        Collections.shuffle(temp);
        Collections.sort(temp, Comparator.comparing(Entity::getSpeed).reversed());

        combatContext = new CombatContext(temp);
    }

    public void startEncounter() {
        while (combatContext.isRunning()) {
            encounterRound();
        }
        endEncounter();
    }

    private void encounterRound() {
        for (int i = 0; i < combatContext.size(); i++) {
            int number = combatContext.next();
            encounterTurn(number);
        }
    }

    private void encounterTurn(int number) {
        Entity entity = combatContext.get(number);
        entity.startOfTurn();
        entity.middleOfTurn();
        entity.endOfTurn();
    }

    private void endEncounter() {
        
    }
}
package com.example.adventure.creature.behaviours;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.adventure.creature.Creature;
import com.example.adventure.creature.NonPlayerCreature;

public class BerzerkerAttackTargeting extends AttackTargeting {
    public BerzerkerAttackTargeting(NonPlayerCreature self) {
        super(self);
    }

    @Override
    public List<Creature> getRankedTargets() {
        HashMap<Long, Float> aggroMap = new HashMap<>(self.getAggroMap());
        HashMap<Long, Creature> validTargets = new HashMap<>(getValidTargets());
        
        if (aggroMap.isEmpty()) {
            return rankRandomly();
        } else {
            // Sort by decaying aggro
            Set<Long> ids = aggroMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

            return validTargets.entrySet().stream()
                    .filter(e -> ids.contains(e.getKey()))
                    .map(Map.Entry::getValue)
                    .toList();
        }
    }
}
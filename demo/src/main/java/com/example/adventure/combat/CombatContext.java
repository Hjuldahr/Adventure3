package com.example.adventure.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.adventure.combat.CombatContext.CombatEntry;
import com.example.adventure.entities.Entity;

public class CombatContext {
    private Map<Integer,Entity> actors = new HashMap<>();
    private Queue<CombatEntry> order = new LinkedList<>();
    private AtomicInteger lastNumber = new AtomicInteger();
    private boolean isRunning = true;

    public record CombatEntry(int combatNumber, Entity actor) {}

    public CombatContext(List<Entity> entities) {
        entities.forEach(this::add);
    }

    public CombatEntry add(Entity entity) {
        int number = lastNumber.incrementAndGet();
        CombatEntry entry = new CombatEntry(number, entity);
        actors.put(number, entity);
        order.add(entry);
        return entry;
    }

    public Optional<Entity> getByNumber(int number) {
        return Optional.ofNullable(actors.get(number));
    }

    public List<CombatEntry> getByAllegiance(boolean isEnemy) {
        return order.stream()
            .filter(e -> e.actor.isEnemy() == isEnemy)
            .toList();
    }

    public List<CombatEntry> getByLiving(boolean isAlive) {
        return order.stream()
            .filter(e -> e.actor.isAlive() == isAlive)
            .toList();
    }

    public List<CombatEntry> getAll() {
        return new ArrayList<>(order);
    }

    public void removeByNumber(int number) {
        actors.remove(number);
        order.removeIf(e -> e.combatNumber == number);
    }

    public CombatEntry peek() {
        return order.peek();
    }

    public Optional<CombatEntry> next() {
        while (isRunning) {
            CombatEntry entry = order.poll();

            if (entry == null) return Optional.empty();

            if (!entry.actor.isAlive() && !entry.actor.canRevive()) {
                removeByNumber(entry.combatNumber);
                continue;
            }

            order.add(entry);

            if (entry.actor.canAct()) {
                return Optional.of(entry);
            }
        }

        return Optional.empty();
    }

    public boolean isRunning() {
        return isRunning;
    }
}
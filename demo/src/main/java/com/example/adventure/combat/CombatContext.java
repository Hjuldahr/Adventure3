package com.example.adventure.combat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.adventure.entities.Entity;

public class CombatContext {
    private HashMap<Integer,Entity> entities = new HashMap<>();
    private Queue<Integer> order = new LinkedList<>();
    private AtomicInteger lastNumber = new AtomicInteger();
    private boolean isRunning = true;

    public record CombatEntry(int combatNumber, Entity entity) {}

    public CombatContext(List<Entity> entities) {
        entities.forEach(this::add);
    }

    public int add(Entity entity) {
        int number = lastNumber.incrementAndGet();
        entities.put(number, entity);
        order.add(number);
        return number;
    }

    public Entity get(int number) {
        return entities.get(number);
    }

    public HashMap<Integer,Entity> getAll() {
        return new HashMap<>(this.entities);
    }

    public void remove(int number) {
        entities.remove(number);
        order.remove(number);
    }

    public int peek() {
        return order.peek();
    }

    public int next() {
        int number = order.poll();
        order.add(number);
        return number;
    }

    public void clear() {
        entities.clear();
        order.clear();
    }

    public int size() {
        return order.size();
    }

    public boolean isRunning() {
        return isRunning;
    }
}
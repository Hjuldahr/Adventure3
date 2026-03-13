package com.example.adventure.utility;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Loop<T> {
    private HashMap<Integer,T> elements;
    private ArrayDeque<Integer> sequence;
    private int nextId = 0;

    @SafeVarargs
    public Loop(T... elements) {
        this(elements.length);
        for (int i = 0; i < elements.length; i++) {
            this.elements.put(i, elements[i]);
            this.sequence.offer(i);
        }
        nextId = elements.length;
    }

    public Loop(List<T> elements) {
        this(elements.size());
        for (int i = 0; i < elements.size(); i++) {
            this.elements.put(i, elements.get(i));
            this.sequence.offer(i);
        }
        nextId = elements.size();
    }

    public Loop(int capacity) {
        this.elements = new HashMap<>(capacity); 
        this.sequence = new ArrayDeque<>(capacity + 1);
    }

    public Loop(Loop<T> other) {
        this.elements = other.elements;
        this.sequence = other.sequence;
    }

    public T next() {
        if (sequence.isEmpty()) return null;
        Integer id = sequence.poll();
        T element = elements.get(id);
        if (element == null) {
            return next();
        }
        sequence.offer(id); // Move to back of the line if still in elements
        return element;
    }

    public void reset() {
        elements.values().removeIf(Objects::isNull);
        if (elements.isEmpty()) return;
        sequence.clear();
        sequence.addAll(elements.keySet().stream().sorted().toList());
    }

    public int getId() {
        return sequence.peek();
    }

    public T get() {
        return elements.get(sequence.peek());
    }

    public void append(T element) {
        elements.put(nextId, element);
        sequence.add(nextId);
        nextId++;
    }

    public void insertAt(int id, T element) {
        if (!elements.keySet().contains(id)) {
            sequence.add(id);
        }
        elements.put(id, element);
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public void removeAt(int id) {
        elements.remove(id);
        sequence.remove(id); 
    }

    public void remove(T element) {
        for (Map.Entry<Integer, T> e : elements.entrySet()) {
            if (Objects.equals(e.getValue(), element)) {
                removeAt(e.getKey());
                return;
            }
        }
    }
}
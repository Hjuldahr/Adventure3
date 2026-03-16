package com.example.adventure.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CyclicList<T> {
    private HashMap<Integer,T> elements;
    private ArrayList<Integer> indices;
    private int indexPointer = 0;
    
    public CyclicList() {
        this(10);
    }
    
    public CyclicList(int initialCapacity) {
        elements = new HashMap<>(initialCapacity);
        indices = new ArrayList<>(initialCapacity);
    }

    public CyclicList(ArrayList<Integer> indices, HashMap<Integer,T> elements) {
        this.indices = new ArrayList<>(indices);
        this.elements = new HashMap<>(elements);
    }

    public CyclicList(CyclicList<T> other) {
        this(other.indices, other.elements);
    }

    public void forEach(Consumer<? super T> action) {
        elements.values().forEach(action);
    }

    public boolean hasNext() {
        return !indices.isEmpty() && indexPointer < indices.size();
    }

    public T next() {
        if (!hasNext()) return null;

        Integer index = indices.get(indexPointer); 
        T element = elements.get(index);
        indexPointer = Math.min(indexPointer + 1, indices.size());

        return element;
    }

    public T peek() {
        if (indices.isEmpty()) return null;

        Integer index = indices.get(indexPointer); 
        T element = elements.get(index);

        return element;
    }

    public void select(int i) {
        indexPointer = Math.clamp(i, 0, indices.size() - 1);
    }

    public void remove(T element) {
        for (Map.Entry<Integer, T> e : elements.entrySet()) {
            if (Objects.equals(e.getValue(), element)) {
                removeAt(e.getKey());
                return;
            }
        }
    }

    public void putNext(Integer index, T element) {
        elements.put(index, element);
        indices.add(index);
    }

    public void removeAt(Integer index) {
        elements.remove(index);
        // Remove all time-slots for this entity
        indices.removeIf(e -> e.equals(index));
        
        // 3. Safety Check: If the list is now shorter than the pointer, reset it
        indexPointer = Math.clamp(indexPointer, 0, indices.size());
    }

    public Collection<T> elements() {
        return elements.values();
    }

    public List<Integer> indices() {
        return indices;
    }
}

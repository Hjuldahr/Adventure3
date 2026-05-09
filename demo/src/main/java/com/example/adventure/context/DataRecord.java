package com.example.adventure.context;

import java.util.HashMap;
import java.util.Map;

public class DataRecord {
    private final Map<ContextKey<?>, Object> values = new HashMap<>();

    public <T> DataRecord set(ContextKey<T> key, T value) {
        values.put(key, value);
        return this; 
    }

    public <T> T get(ContextKey<T> key) {
        Object value = values.get(key);
        // Returns the value cast to T, or null if not found
        return (value == null) ? null : key.type().cast(value);
    }
    
    public boolean has(ContextKey<?> key) {
        return values.containsKey(key);
    }
}
package com.example.adventure.context;

import java.util.HashMap;
import java.util.Map;

public class DataRecord {
    private final Map<ContextKey<?>, Object> values = new HashMap<>();

    public DataRecord() {}

    public DataRecord(DataRecord other) {
        if (other != null) {
            this.values.putAll(other.values);
        }
    }

    public <T> DataRecord set(ContextKey<T> key, T value) {
        values.put(key, value);
        return this; 
    }

    public DataRecord set(ContextKey<Flag> key) {
        values.put(key, Flag.value);
        return this; 
    }

    public <T> DataRecord unset(ContextKey<T> key) {
        values.remove(key);
        return this; 
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ContextKey<T> key) {
        Object value = values.get(key);
        if (value == null) return null;
        return (T) value;
    }
    
    public boolean has(ContextKey<?> key) {
        return values.containsKey(key);
    }
}
package com.example.adventure.context;

public record ContextKey<T>(String id, Class<T> type) {
    public static <T> ContextKey<T> of(String id, Class<T> dataType) {
        return new ContextKey<>(id, dataType);
    }
}
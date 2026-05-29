package com.example.adventure.context;

import java.util.Objects;

public record ContextKey<T>(String id) {
    /**
     * ContextKey factory
     * @param <T> dataType
     * @param dataType dataType class
     * @return ContextKey
     */
    public static <T> ContextKey<T> named(String id) {
        return new ContextKey<>(Objects.requireNonNull(id));
    }
}
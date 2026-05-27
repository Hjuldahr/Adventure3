package com.example.adventure.context;

public record ContextKey<T>(String id, Class<T> type) {
    /**
     * ContextKey factory
     * @param <T> dataType
     * @param id globally unique identifier
     * @param dataType dataType class
     * @return ContextKey
     */
    public static <T> ContextKey<T> of(String id, Class<T> dataType) {
        return new ContextKey<>(id, dataType);
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof ContextKey<?> that && this.id.equals(that.id));
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
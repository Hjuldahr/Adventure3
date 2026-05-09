package com.example.adventure.actions;

public abstract class Action<T extends Record> {
    private final String name;

    public Action(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    /**
     * Perform an action
     * @return result data
     */
    abstract public T trigger();
}

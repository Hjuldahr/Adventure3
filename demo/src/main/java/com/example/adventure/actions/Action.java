package com.example.adventure.actions;

import com.example.adventure.context.DataRecord;

public abstract class Action {
    private final String name;

    public Action(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    /**
     * Perform an action
     * @return result data
     */
    abstract public DataRecord trigger();
}

package com.example.adventure.events;

import java.util.function.Consumer;

import com.example.adventure.context.DataRecord;

public class EventContainer<T extends Enum<T>> {
    private final T eventType;
    private final Consumer<DataRecord> eventLambda;

    public EventContainer(T eventType, Consumer<DataRecord> eventLambda) {
        this.eventType = eventType;
        this.eventLambda = eventLambda;
    }
    public T getEventType() { return eventType; }
    public Consumer<DataRecord> getLambda() { return eventLambda; }
    public void fire(DataRecord params) { eventLambda.accept(params); }
}
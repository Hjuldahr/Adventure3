package com.example.adventure.events;

import java.util.function.Consumer;

import com.example.adventure.context.DataRecord;

public class EventContainer {
    private final EventTypes eventType;
    private final Consumer<DataRecord> eventLambda;

    public EventContainer(EventTypes eventType, Consumer<DataRecord> eventLambda) {
        this.eventType = eventType;
        this.eventLambda = eventLambda;
    }
    public EventTypes getEventType() { return eventType; }
    public Consumer<DataRecord> getLambda() { return eventLambda; }
    public void fire(DataRecord params) { eventLambda.accept(params); }
}
package com.example.adventure.events;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.example.adventure.context.DataRecord;

public class EventListener 
{
    private EnumMap<EventTypes,Set<EventContainer>> eventBindings;

    /**
     * Constructor
     */
    public EventListener() {
        this.eventBindings = new EnumMap<>(EventTypes.class);
    }

    /**
     * Cloning Constructor
     * @param other
     */
    public EventListener(EventListener other) {
        this.eventBindings = new EnumMap<>(EventTypes.class);
        // unlink shallow memory references
        other.eventBindings.forEach((k, v) -> this.eventBindings.put(k, new HashSet<>(v)));
    }

    /**
     * Bind a single event
     * @param event
     */
    public void bind(EventContainer event) {
        if (event == null) return;
        eventBindings.computeIfAbsent(event.getEventType(), (k) -> new HashSet<>()).add(event);
    }

    /**
     * Bind multiple events
     * @param events
     */
    public void bindAll(Collection<EventContainer> events) {
        if (events != null) {
            events.forEach(this::bind);
        }
    }

    /**
     * Bind multiple events
     * @param events
     */
    public void bindAll(EventContainer[] events) {
        if (events != null) {
            Arrays.stream(events).forEach(this::bind);
        }
    }

    /**
     * Unbind a single events
     * @param event
     */
    public void unbind(EventContainer event) {
        if (event == null) return;
        eventBindings.computeIfPresent(event.getEventType(), (_, v) -> {
            v.remove(event);
            return v.isEmpty() ? null : v;
        });
    }

    /**
     * Unbind multiple events
     * @param events
     */
    public void unbindAll(Collection<EventContainer> events) {
        if (events != null) {
            events.forEach(this::unbind);
        }
    }

    /**
     * Unbind multiple events
     * @param events
     */
    public void unbindAll(EventContainer[] events) {
        if (events != null) {
            Arrays.stream(events).forEach(this::unbind);
        }
    }

    /**
     * Unbind all events by type
     * @param eventType
     */
    public void unbindAll(EventTypes eventType) {
        eventBindings.remove(eventType);
    }

    /**
     * Unbind all events
     */
    public void unbindAll() {
        eventBindings.clear();
    }

    /**
     * Fire all events by type
     * @param eventType
     * @param params
     */
    public void fire(EventTypes eventType, DataRecord params) {
        get(eventType).forEach(e -> e.fire(params));
    }

    /**
     * Fire all events by type, using the default events if no events of that type were bound
     * @param eventType
     * @param defaultEvents
     * @param params
     */
    public void fire(EventTypes eventType, Set<EventContainer> defaultEvents, DataRecord params) {
        get(eventType, defaultEvents).forEach(e -> e.fire(params));
    }

    /**
     * Fetch all events by type, returning an empty set if no events of that type were bound
     * @param eventType
     * @return bound events or empty set
     */
    public Set<EventContainer> get(EventTypes eventType) {
        return get(eventType, Set.of());
    }

    /**
     * Fetch all events by type, returning the default set if no events of that type were bound
     * @param eventType
     * @param defaultEvents
     * @return bound events or default events
     */
    public Set<EventContainer> get(EventTypes eventType, Set<EventContainer> defaultEvents) {
        Objects.requireNonNull(defaultEvents, "Default Events must be non null");
        return Set.copyOf(eventBindings.getOrDefault(eventType, defaultEvents));
    }
}
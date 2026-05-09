package com.example.adventure.events;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.example.adventure.context.DataRecord;

public class EventListener<K extends Enum<K>> 
{
    private EnumMap<K,Set<EventContainer<K>>> eventBindings;

    /**
     * Constructor
     * @param clazz
     */
    public EventListener(Class<K> clazz) {
        this.eventBindings = new EnumMap<>(clazz);
    }

    /**
     * Cloning Constructor
     * @param other
     */
    public EventListener(EventListener<K> other) {
        this.eventBindings = new EnumMap<>(other.eventBindings);
        // unbind
        this.eventBindings.replaceAll((key, set) -> new HashSet<>(set));
    }

    /**
     * Bind a single event
     * @param event
     */
    public void bind(EventContainer<K> event) {
        if (event == null) return;
        eventBindings.computeIfAbsent(event.getEventType(), (k) -> new HashSet<>()).add(event);
    }

    /**
     * Bind multiple events
     * @param events
     */
    public void bindAll(Collection<EventContainer<K>> events) {
        if (events != null) {
            events.forEach(this::bind);
        }
    }

    /**
     * Bind multiple events
     * @param events
     */
    public void bindAll(EventContainer<K>[] events) {
        if (events != null) {
            Arrays.stream(events).forEach(this::bind);
        }
    }

    /**
     * Unbind a single events
     * @param event
     */
    public void unbind(EventContainer<K> event) {
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
    public void unbindAll(Collection<EventContainer<K>> events) {
        if (events != null) {
            events.forEach(this::unbind);
        }
    }

    /**
     * Unbind multiple events
     * @param events
     */
    public void unbindAll(EventContainer<K>[] events) {
        if (events != null) {
            Arrays.stream(events).forEach(this::unbind);
        }
    }

    /**
     * Unbind all events by type
     * @param eventType
     */
    public void unbindAll(K eventType) {
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
    public void fire(K eventType, DataRecord params) {
        get(eventType).forEach(e -> e.fire(params));
    }

    /**
     * Fire all events by type, using the default events if no events of that type were bound
     * @param eventType
     * @param defaultEvents
     * @param params
     */
    public void fire(K eventType, Set<EventContainer<K>> defaultEvents, DataRecord params) {
        get(eventType, defaultEvents).forEach(e -> e.fire(params));
    }

    /**
     * Fetch all events by type, returning an empty set if no events of that type were bound
     * @param eventType
     * @return bound events or empty set
     */
    public Set<EventContainer<K>> get(K eventType) {
        return get(eventType, Set.of());
    }

    /**
     * Fetch all events by type, returning the default set if no events of that type were bound
     * @param eventType
     * @param defaultEvents
     * @return bound events or default events
     */
    public Set<EventContainer<K>> get(K eventType, Set<EventContainer<K>> defaultEvents) {
        Objects.requireNonNull(defaultEvents, "Default Events must be non null");
        return Set.copyOf(eventBindings.getOrDefault(eventType, defaultEvents));
    }
}
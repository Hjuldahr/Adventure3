package com.example.adventure.events;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class EventListener 
{
    private EnumMap<EventTypes,HashSet<Consumer<?>>> eventBindings;

    public EventListener() {

    }

    public void bind(EventTypes eventType, Consumer<?> func) {
        if (func == null) return;

        eventBindings.compute(eventType, (key, val) -> {
            if (val == null) { return new HashSet<>(Set.of(func)); } 
            else { val.add(func); return val; }
        });
    }

    public void bindAll(EventTypes eventType, Consumer<?> ...funcs) {
        bindAll(eventType, Set.of(funcs));
    }

    public void bindAll(EventTypes eventType, Collection<Consumer<?>> funcs) {
        if (funcs == null || funcs.isEmpty()) return;
        
        eventBindings.compute(eventType, (key, val) -> {
            if (val == null) { return new HashSet<>(funcs); } 
            else { val.addAll(funcs); return val; }
        });
    }

    public void unbind(EventTypes eventType, Consumer<?> func) {
        if (func == null) return;

        eventBindings.computeIfPresent(eventType, (_, val) -> {
            val.remove(func);
            return val.isEmpty() ? null : val;
        });
    }

    public void unbindAll(EventTypes eventType, Consumer<?> ...funcs) {
        unbindAll(eventType, Set.of(funcs));
    }

    public void unbindAll(EventTypes eventType, Collection<Consumer<?>> funcs) {
        eventBindings.computeIfPresent(eventType, (_, val) -> {
            val.removeAll(funcs);
            return val.isEmpty() ? null : val;
        });
    }

    public void unbindAll(EventTypes eventType) {
        eventBindings.remove(eventType);
    }

    public void unbindAll() {
        eventBindings.clear();
    }

    public Set<Consumer<?>> getBinding(EventTypes eventType) {
        Set<Consumer<?>> bindings = eventBindings.get(eventType);
        return bindings == null ? Set.of() : Set.copyOf(bindings);
    }

    public Set<Consumer<?>> getBinding(EventTypes eventType, Collection<Consumer<?>> defaultEvents) {
        Set<Consumer<?>> bindings = eventBindings.get(eventType);
        return (bindings == null || bindings.isEmpty()) 
            ? Set.copyOf(defaultEvents) 
            : Set.copyOf(bindings);
    }
}
package com.example.adventure.effect;

import com.example.adventure.context.DataRecord;

public class Effect {
    private final DataRecord params;

    private String name;
    private int duration;

    public Effect(DataRecord params, String name, int duration) {
        this.params = new DataRecord(params); 
        this.name = name;
        this.duration = duration;
    }

    public Effect(Effect other) {
        this.params = new DataRecord(other.params);
        this.name = other.name;
        this.duration = other.duration;
    }

    public DataRecord getParams() { return params; }
    public void decrementDuration() { duration--; }
    public boolean isExpired() { return duration <= 0; }
    public String getName() { return name; }

    public Effect merge(Effect other) {
        if (!equals(other)) {
            throw new IllegalArgumentException("Cannot merge different effects: " + this.name + " and " + other.name);
        }
        Effect effect =  new Effect(this);
        effect.duration = Math.max(this.duration, other.duration);
        return effect;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (!(other instanceof Effect otherEffect)) return false;

        return this.name.equals(otherEffect.name);
    }
}
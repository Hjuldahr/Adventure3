package com.example.adventure.entity.behaviours;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.example.adventure.entity.Entity;

public abstract class Targeting {
    protected Entity self;

    protected Targeting(Entity self) {
        this.self = self;
    }

    public abstract List<Entity> getRankedTargets();

    protected Stream<Entity> validTargetsStream() {
        return getValidTargets().stream();
    }

    abstract protected Set<Entity> getValidTargets();
}

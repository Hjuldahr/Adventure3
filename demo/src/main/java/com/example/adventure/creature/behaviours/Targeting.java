package com.example.adventure.creature.behaviours;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.example.adventure.creature.Creature;

public abstract class Targeting {
    protected Creature self;

    protected Targeting(Creature self) {
        this.self = self;
    }

    public abstract List<Creature> getRankedTargets();

    protected Stream<Creature> validTargetsStream() {
        return getValidTargets().stream();
    }

    abstract protected Set<Creature> getValidTargets();
}

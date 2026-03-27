package com.example.adventure.action;

import java.util.List;

import com.example.adventure.creature.Creature;

public abstract class Action {
    public enum ActivationCost {
        // Unlimited
        FREE_ACTION("Free Action"), 
        // Standard Actions
        MOVEMENT("Movement"), 
        BONUS_ACTION("Bonus Action"), 
        ACTION("Action"),
        // Conditional Actions
        REACTION("Reaction"), 
        LEGENDARY_ACTION("Legendary Action"),
        LAIR_ACTION("Lair Action");

        private final String name;

        ActivationCost(String name) {
            this.name = name;
        }

        public String getName() { return this.name; }
    }
    
    protected final String name;
    protected final ActivationCost cost;

    public Action(String name, ActivationCost cost) {
        this.name = name;
        this.cost = cost;
    }

    public Action(String name) {
        this(name, ActivationCost.ACTION);
    }

    public abstract boolean perform(Creature actor, List<Creature> targets);

    public ActivationCost getCost() { return this.cost; }
}

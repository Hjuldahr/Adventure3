package com.example.adventure.entity;

public abstract class NonPlayerEntity extends Entity
{
    public NonPlayerEntity(
        String name
    ) {
        super(name);
    }

    public NonPlayerEntity(NonPlayerEntity other) {
        super(other);
    }
}

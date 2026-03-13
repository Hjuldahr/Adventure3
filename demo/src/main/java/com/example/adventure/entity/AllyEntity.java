package com.example.adventure.entity;

public class AllyEntity extends NonPlayerEntity
{
    public AllyEntity(
        String name
    ) {
        super(name);
    }

    public AllyEntity(AllyEntity other) {
        super(other);
    }
}
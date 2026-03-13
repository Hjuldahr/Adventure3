package com.example.adventure.entity;

public class EnemyEntity extends NonPlayerEntity
{
    public EnemyEntity(
        String name
    ) {
        super(name);
    }

    public EnemyEntity(EnemyEntity other) {
        super(other);
    }
}

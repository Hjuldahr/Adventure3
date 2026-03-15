package com.example.adventure.entity;

import com.example.adventure.currency.CoinPurse;
import com.example.adventure.item.Inventory;

public abstract class NonPlayerEntity extends Entity
{
    private int armourClass;
    
    public NonPlayerEntity(
        String name
    ) {
        super(name);
    }

    public NonPlayerEntity(NonPlayerEntity other) {
        super(other);
    }

    public int getArmourClass() {
        return armourClass;
    }
}

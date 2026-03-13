package com.example.adventure.entity;

import com.example.adventure.currency.CoinPurse;
import com.example.adventure.item.Inventory;

public abstract class NonPlayerEntity extends Entity
{
    private Inventory inventory;
    private CoinPurse coinPurse;
    
    public NonPlayerEntity(
        String name
    ) {
        super(name);
    }

    public NonPlayerEntity(NonPlayerEntity other) {
        super(other);
    }
}

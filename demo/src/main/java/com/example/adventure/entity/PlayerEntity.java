package com.example.adventure.entity;

import com.example.adventure.currency.CoinPurse;
import com.example.adventure.item.Inventory;

public class PlayerEntity extends Entity
{
    private Inventory inventory;
    private CoinPurse coinPurse;
    
    public PlayerEntity(
        String name
    ) {
        super(name);
    }

    @Override
    public int getArmourClass() {
        //
    }
}

package com.example.adventure.entity;

import com.example.adventure.currency.CoinPurse;
import com.example.adventure.entity.AbilityScores.AbilityCategories;
import com.example.adventure.item.Armoury;
import com.example.adventure.item.Inventory;

public class PlayerEntity extends Entity
{
    private Armoury armoury;
    private Inventory inventory;
    private CoinPurse coinPurse;
    
    public PlayerEntity(
        String name
    ) {
        super(name);
    }

    @Override
    public int getArmourClass() {
        return armoury.getArmourClass() + abilityScores.getModifier(AbilityCategories.AGILITY);
    }

    @Override
    public void turn() {
        super.turn();


        
    }
}

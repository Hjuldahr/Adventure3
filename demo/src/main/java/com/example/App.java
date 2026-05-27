package com.example;

import com.example.adventure.categories.DamageTypes;
import com.example.adventure.context.DataRecord;
import com.example.adventure.context.Keys;
import com.example.adventure.entities.Entity;
import com.example.adventure.entities.NPC;
import com.example.adventure.entities.Player;

public class App 
{
    public static void main( String[] args )
    {
        Entity testA = new Player("Alphonse", 100);
        Entity testB = new NPC("Gobbo", 100);

        DataRecord attackData = new DataRecord()
            .set(Keys.ATTACK_NAME, "Mind Bolt")
            .set(Keys.ATTACK_POWER, 5)
            .set(Keys.DAMAGE_TYPE, DamageTypes.PSIONIC)
            .set(Keys.TARGET, testB);

        testA.performSpellAttack(attackData);
    }
}

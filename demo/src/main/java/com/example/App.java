package com.example;

import com.example.adventure.categories.DamageTypes;
import com.example.adventure.context.DataRecord;
import com.example.adventure.context.Keys;
import com.example.adventure.entities.Entity;
import com.example.adventure.entities.NPC;
import com.example.adventure.entities.Player;
import com.example.adventure.entities.Pronouns;

public class App 
{
    public static void main( String[] args )
    {
        Entity testA = new Player("Alphonse", Pronouns.MALE, 100);
        Entity testB = new NPC("Gobbi", Pronouns.XENO, 100);

        DataRecord attackData = new DataRecord()
            .set(Keys.ATTACK_NAME, "Mind Bolt")
            .set(Keys.ATTACK_POWER, 5)
            .set(Keys.DAMAGE_TYPE, DamageTypes.PSIONIC)
            .set(Keys.TARGET, testB);

        testA.performSpellAttack(attackData);
    }
}

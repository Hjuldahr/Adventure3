package com.example;

import com.example.adventure.context.DataRecord;
import com.example.adventure.context.Keys;
import com.example.adventure.entities.DamageTypes;
import com.example.adventure.entities.Entity;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Entity testA = new Entity("A", 100);
        Entity testB = new Entity("B", 100);

        DataRecord attackData = new DataRecord()
            .set(Keys.DAMAGE, 10)
            .set(Keys.DAMAGE_TYPE, DamageTypes.PHYSICAL)
            .set(Keys.TARGET, testB);

        testA.performWeaponAttack(attackData);
    }
}

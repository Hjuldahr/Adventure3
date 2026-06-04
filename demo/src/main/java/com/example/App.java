package com.example;

import com.example.adventure.categories.PlayerTurnActions;
import com.example.adventure.utilities.Terminal;

public class App 
{
    public static void main( String[] args )
    {
        /* 
        Entity testA = new Player("Alphonse", Pronouns.MALE, 100);
        Entity testB = new NPC("Gobbi", Pronouns.XENO, 100);

        DataRecord attackData = new DataRecord()
            .set(Keys.ATTACK_NAME, "Mind Bolt")
            .set(Keys.ATTACK_POWER, 5)
            .set(Keys.DAMAGE_TYPE, DamageTypes.PSIONIC)
            .set(Keys.TARGET, testB);

        testA.performSpellAttack(attackData);
        */
        PlayerTurnActions result = Terminal.inputOptions("> ", PlayerTurnActions.class);
        System.out.println(result);
    }
}

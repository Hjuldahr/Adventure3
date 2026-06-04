package com.example.adventure.entities;

import com.example.adventure.categories.AbilityTypes;
import com.example.adventure.categories.PlayerTurnActions;
import com.example.adventure.combat.CombatContext;
import com.example.adventure.utilities.Terminal;

public class Player extends Entity {
    public static final int MIN_HP = 100;
    public static final int MAX_HP = 1_000;
    
    public final int LEVEL_COST_SCALE = 100;

    private int experience;
    private boolean isStunned;
    private boolean isDowned;
    
    public Player(String name, Pronouns pronoun, int maxHP) {
        super(name, pronoun, maxHP);
    }

    public void addExperience(int experience) {
        this.experience += experience;

    }

    public int getLevelThreshold() {
        // current level * 110
        return level * LEVEL_COST_SCALE;
    }

    public void viewSheet() {
        System.out.printf("Name: %s\n\n", this.name);

        System.out.printf("HP: %s\n", this.hitPoints);
        System.out.printf("MP: %s\n\n", this.magicPoints);

        System.out.printf("Lvl: %s\n", this.level);
        System.out.printf("Current Exp: %s\n", this.experience);
        System.out.printf("To Next Level: %s\n\n", getLevelThreshold() - this.experience);

        System.out.printf("Accuracy: %s%%\n", this.accuracy * 100);
        System.out.printf("Evasion: %s%%\n", this.evasion * 100);
        System.out.printf("Defence: %s\n", getDefenceBonus());

        abilityScores.displayScores();
    }

    public int recalculateMaxHP() {
        // 100 to 1,000 HP
        int fortitude = abilityScores.getScore(AbilityTypes.FORTITUDE);
        float range = (float) (MAX_HP - MIN_HP);
        float rawHP = MIN_HP + (fortitude - 1.0f) * (range / 99.0f);
        int adjustedHP = Math.round(rawHP / 5.0f) * 5;

        hitPoints.setMaxHP(adjustedHP);

        return adjustedHP;
    }

    @Override
    protected int getDefenceBonus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDefenceBonus'");
    }

    @Override
    public void onTurn(CombatContext combatContext) {
        if (isStunned || isDowned) {
            System.out.println("You cannot ACT this turn.");
            Terminal.inputNull();
            return;
        }
        
        boolean turnOver = false;
        PlayerTurnActions[] options = PlayerTurnActions.values();

        while (!turnOver) {
            for (int i = 0; i < options.length; i++) {
                System.out.printf("[%d] %s\n", i+1, options[i].name());
            }

            PlayerTurnActions option = Terminal.inputOptions("> ", options);

            turnOver = switch (option) {
                case ATTACK -> playerAttack(combatContext):
                case MAGIC -> playerMagic(combatContext):  
                case ITEM -> {
                    playerItem();
                    yield false;
                }:   
                case STATS -> {
                    playerStats();
                    yield false;
                }:
            };
        }
    }

    public boolean playerAttack(CombatContext combatContext) {

    }

    public boolean playerMagic(CombatContext combatContext) {
        
    }

    public boolean playerItem(CombatContext combatContext) {
        
    }

    public void playerStats() {

    }
}

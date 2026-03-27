package com.example.adventure.creature;

import java.util.stream.IntStream;

import com.example.adventure.randomizer.Dice;

public class HitPoints {
    private int currentDice;
    private int maximumDice;
    private Dice hitDie;
    private int maximumHP;
    private int currentHP;
    private int currentTemporaryHP;

    /**
     * Example: 3d8+3, allows you to spend upto 3 d8+3 to heal
     * @param hitDice
     */
    public HitPoints(Dice hitDice) {
        this.currentDice = hitDice.getCount();
        this.maximumDice = currentDice;
        this.hitDie = new Dice(
            hitDice.getSides()
        );
        this.currentHP = hitDice.roll();
        this.maximumHP = currentHP;
        this.currentTemporaryHP = 0;
    }
    public String getCounter() {
        return String.format("%d / %d HP", getEffectiveHP(), this.maximumHP);
    }
    public String getMeter() {
        float ratio = getRatio();
        return String.format(ratio <= 0.01f ? "%.1f%% HP" : "%.0f%% HP", ratio * 100);
    }
    public void spendHitDice(int count, int modifier) {
        if (this.currentDice < count) return;
        
        this.currentDice = Math.max(0, this.currentDice - count);
        int result = IntStream.range(0, count).map(i -> hitDie.rawRoll() + modifier).sum();
        addHP(result);
    }
    public void resetHitDice() {
        this.currentDice = this.maximumDice;
    }
    public float getRatio() {
        if (this.maximumHP <= 0) return 0f;
        return (float) getEffectiveHP() / (float) this.maximumHP;
    }
    public int takeDamage(int damage) {
        if (this.currentTemporaryHP > 0) {
            int absorbed = Math.min(this.currentTemporaryHP, damage);
            this.currentTemporaryHP -= absorbed;
            damage -= absorbed;
        }
        int remainingHP = this.currentHP - damage;
        this.currentHP = Math.max(0, remainingHP);
        return remainingHP;
    }
    public void addHP(int healing) {
        this.currentHP = Math.min(this.currentHP + healing, this.maximumHP);
    }
    public int getHP() {
        return this.currentHP;
    }
    public int getTempHP() {
        return this.currentTemporaryHP;
    }
    public int getEffectiveHP() {
        return this.currentHP + this.currentTemporaryHP;
    }
    public int getMaxHP() {
        return this.maximumHP;
    }
    public void setHitDieCount(int count) {
        this.maximumDice = count;
        this.currentDice = count;
    }
    public int getHitDiceCount() {
        return this.currentDice;
    }
    public int getMaximumHitDiceCount() {
        return this.maximumDice;
    }
    public Dice getHitDie() {
        return this.hitDie;
    }
    public void minimizeHP() {
        this.currentHP = 0;
        this.currentTemporaryHP = 0;
    }
    public void maximizeHP() {
        this.currentHP = this.maximumHP;
    }
    public void setTempHP(int tempHP) {
        this.currentTemporaryHP = tempHP;
    }
    public void resetTempHP() {
        this.currentTemporaryHP = 0;
    }
    public boolean atMaximum() {
        return this.currentHP == this.maximumHP;
    }
    public boolean atZero() {
        return this.currentHP == 0;
    }
    public boolean isBloodied() {
        return this.currentHP < this.maximumHP / 2;
    }
}
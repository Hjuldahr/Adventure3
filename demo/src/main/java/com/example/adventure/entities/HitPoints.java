package com.example.adventure.entities;

public class HitPoints {
    private int currentHP;
    private int maxHP;

    public HitPoints(int maxHP) {
        this(maxHP, maxHP);
    }

    public HitPoints(int currentHP, int maxHP) {
        this.currentHP = currentHP;
        this.maxHP = maxHP;
    }

    public int getCurrentHP() { return currentHP; }
    public void setCurrentHP(int currentHP) { this.currentHP = Math.clamp(currentHP, 0, this.maxHP); }

    public int getMaxHP() { return maxHP; }
    public void setMaxHP(int maxHP) { 
        this.maxHP = maxHP;  
        this.currentHP = Math.min(this.currentHP, this.maxHP);
    }

    public void change(int change) {
        this.setCurrentHP(this.currentHP + change);
    }

    public int reversed() {
        return this.maxHP - this.currentHP;
    }

    public float ratio() {
        return this.currentHP / (float) this.maxHP;
    }

    public boolean atMinimum() { return this.currentHP == 0; }
    public boolean atMaximum() { return this.currentHP == this.maxHP; }

    public void minimize() { this.currentHP = 0; }
    public void maximize() { this.currentHP = this.maxHP; }

    @Override
    public String toString() {
        return "%s / %S HP".formatted(this.currentHP, this.maxHP);
    }
}

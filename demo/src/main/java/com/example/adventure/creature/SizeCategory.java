package com.example.adventure.creature;

public enum SizeCategory {
    TINY("Tiny"),
    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large"),
    HUGE("Huge"),
    GARGANTUAN("Gargantuan");

    private final String name;

    SizeCategory(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    /**
     * Return positive if large then othe creature. 
     * Return negative if smaller. 
     * Return 0 if identical.
     * Absolute value indicates magnitude of difference.
     * @param other
     * @return
     */
    public int delta(SizeCategory other) {
        return this.ordinal() - other.ordinal();
    }
}

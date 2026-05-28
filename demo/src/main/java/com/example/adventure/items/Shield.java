package com.example.adventure.items;

public class Shield extends Armour {
    public Shield(String name, int buyCost) {
        super(name, buyCost);
    }
    
    public Shield(Shield other) {
        super(other);
    }
}

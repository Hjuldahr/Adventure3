package com.example.adventure.items;

public class Shield extends Garment {


    public Shield(String name, int buyCost, int level) {
        super(name, buyCost, level);
    }
    
    public Shield(Shield other) {
        super(other);
    }
}

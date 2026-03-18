package com.example.adventure.item;

public class Item {
    
    
    public String name;

    public int buyCost;
    public int sellCost;

    private String verb;

    public Item(String name, String verb) {
        this.name = name;
        this.verb = verb;
    }

    public Item(String name) {
        this(name, "holding");
    }

    public String view() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'view'");
    }

    public String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    public boolean requiresBoth() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'requiresBoth'");
    }

    public boolean canOff() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canOff'");
    }

    public boolean canMain() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canMain'");
    }

    public boolean isVersatile() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isVersatile'");
    }

    public String getVerb() {
        return verb;
    }
}

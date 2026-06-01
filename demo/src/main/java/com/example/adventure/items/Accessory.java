package com.example.adventure.items;

public class Accessory extends Equipment {
    protected BodyParts associatedBodyPart;
    
    public Accessory(Accessory other) {
        super(other);
    }

    public BodyParts getAssociatedBodyPart() {
        return associatedBodyPart;
    }

    public int getArmouredDefenceBonus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArmouredDefenceBonus'");
    }

    public int getUnarmouredDefenceBonus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArmouredDefenceBonus'");
    }
}

package com.example.adventure.items;

public class Accessory extends Equipment {
    protected BodyParts associatedBodyPart;
    
    public Accessory(Accessory other) {
        super(other);
    }

    public BodyParts getAssociatedBodyPart() {
        return associatedBodyPart;
    }
}

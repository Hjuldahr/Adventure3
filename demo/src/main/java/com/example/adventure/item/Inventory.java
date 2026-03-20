package com.example.adventure.item;

import com.example.adventure.utility.Colours;
import java.util.Map;
import java.util.TreeMap;

public class Inventory {
    private final Map<Item, Integer> content = new TreeMap<>();

    public void add(Item item) {
        content.merge(item, 1, Integer::sum);
    }

    public void add(Item item, int count) {
        if (count <= 0) return;
        content.merge(item, count, Integer::sum);
    }

    public boolean remove(Item item) {
        return content.computeIfPresent(item, (k, v) -> v > 1 ? v - 1 : null) != null;
    }

    public boolean remove(Item item, int count) {
        if (count <= 0 || content.getOrDefault(item, 0) < count) return false;

        return content.computeIfPresent(item, (k, v) -> v > count ? v - count : null) != null;
    }

    public int getQuantity(Item item) {
        return content.getOrDefault(item, 0);
    }

    public boolean hasItem(Item item) {
        return content.containsKey(item);
    }

    // distinct from armour which is specialized for weapon categories
    public void viewInventory(int carryLimit) {
        int totalWeight = 0;
        int totalItems = 0;
        ItemRarities previousCategory = null;

        for (Map.Entry<Item, Integer> entry : content.entrySet()) {
            Item item = entry.getKey();
            ItemRarities rarity = item.getRarity();   
            int qty = entry.getValue();
            totalItems += qty;
            int weight = item.getWeight() * qty;
            totalWeight += weight;
            
            String qtyText = (qty > 1) ? " x" + qty : ""; 
            
            String buyDisplay = (item.getBuyCost() <= 0) ? "*" : String.valueOf(item.getBuyCost() * qty);
            String sellDisplay = (item.getSellCost() <= 0) ? "*" : String.valueOf(item.getSellCost() * qty);
            String costText = buyDisplay + "/" + sellDisplay;

            if (previousCategory == null || previousCategory != rarity) {
                previousCategory = rarity;
                System.out.printf("-=-=-=-: %s%s%s :-=-=-=-\n", 
                    rarity.getColour(), rarity.getName(), Colours.RESET
                );
            }

            // Changed %d/%d to %s to match your costText string
            System.out.printf("%s%s%s%s\n\tCost: %s\n\tWeight: %d lb\n", 
                rarity.getColour(), 
                item.getName(), 
                qtyText,
                Colours.RESET,
                costText, // This now takes the %s slot
                weight
            );
        }

        System.out.printf("Total\n\t%d Items\n\t%d/%d lb", 
            totalItems, 
            totalWeight, carryLimit
        );
    }
}
package com.example.adventure.creature.behaviours;

/*
 * The goal is to keep it simple and self contained
 * Both enemies and allies
 * Slayer (Attack): lowest current hp -> lowest max hp -> lowest level
 * Hunter (Attack) : highest max hp -> lowest current hp -> highest level
 * Berzerker (Attack) highest damage *taken* per enemy (uses decaying aggro map) => random
 * Assassin (Attack) priotizes by (total damage output + total healing output * 1.5) *dealt* per enemy (uses non-decaying aggro map - because it "pursues" targets) => lowest current HP -> highest level 
 * Battle_Healer
 *   1. if self has % HP < 50%, perform highest HP heal spell on self not on cooldown
 *   2. If > 1 ally has % HP < 50%, perform highest HP Group heal spell not on cooldown 
 *   3. If all group heal spells are on cooldown perform highest HP heal spell not on cooldown on member with lowest HP %.  
 *   4. If exactly 1 ally has % HP < 50%, perform highest HP heal spell on target not on cooldown
 *   5. Fallback to Berzerker logic.
 * Support_Healer
 *   1. if self has % HP < 10%, perform highest HP heal spell on self not on cooldown
 *   2. If > 1 ally has % HP < 90%, perform highest HP Group heal spell not on cooldown 
 *   3. If > 1 ally has % HP < 90% & all group heal spells are on cooldown perform highest HP heal spell not on cooldown on member with lowest HP %.  
 *   4. If exactly 1 ally has % HP < 90%, perform highest HP heal spell on target not on cooldown
 *   5. If no heals or healable individuals are available, skip turn.
 */
public enum RoleTypes {
    SLAYER, 
    HUNTER, 
    BERZERKER, 
    ASSASSIN, 
    BATTLE_HEALER, 
    SUPPORT_HEALER
}
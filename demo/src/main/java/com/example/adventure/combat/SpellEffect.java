package com.example.adventure.combat;

import com.example.adventure.action.spells.Spell;
import com.example.adventure.creature.Creature;

public class SpellEffect {
    private final Spell spell;
    private final Creature caster;
    private final Creature target;
    private final Condition condition;

    private int duration;

    public SpellEffect(Spell spell, Creature caster, Creature target, Condition condition, int duration) {
        this.spell = spell;
        this.caster = caster;
        this.target = target;
        this.condition = condition;
        this.duration = duration;
    }

    public SpellEffect(Spell spell, Creature caster, Creature target, Condition condition) {
        this(spell, caster, target, condition, 0);
    }

    public void dispose() {
        target.removeEffect(this);
    }

    public void decay() {
        duration--;
        
        // 1. Check if the caster still exists and is concentrating on THIS effect
        if (spell.requiresConcentration()) {
            if (caster.getConcentrationEffect() != this) {
                dispose();
                return;
            }
        }

        // 2. Check if the target made a recurring save (e.g., Hold Person)
        if (spell.hasRecurringSave()) {
            if (target.simpleSaveCheck(caster.getSpellSaveDifficulty(), spell.getSaveType())) {
                dispose();
                return;
            }
        }

        // 3. Natural expiration
        if (duration <= 0) {
            dispose();
        }
    }

    public Condition condition() {
        return condition;
    }

    public Spell spell() {
        return spell;
    }

    public Creature target() {
        return target;
    }
}
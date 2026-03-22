package com.example.adventure.action.spells;

import com.example.adventure.combat.SpellEffect;
import com.example.adventure.entity.Ability.AbilityTypes;
import com.example.adventure.entity.Entity;
import java.util.List;

public abstract class Spell {

    public enum ActivationType {
        ACTION,
        BONUS_ACTION,
        FREE_ACTION,
        REACTION
    }

    public enum AreaType {
        SELF,
        SINGLE_TARGET,
        MULTI_TARGET,
        AOE
    }

    protected String name;
    protected int spellLevel;

    protected ActivationType activationType;
    protected AreaType areaType;
    protected int multiTargetLimit;
    protected int duration;

    protected SpellEffect effect;

    protected boolean requiresConcentration;
    protected boolean noDamageOnSave;
    protected boolean noEffectOnSave;
    protected boolean recurringSave;
    protected AbilityTypes saveType;

    //upcasting

    public Spell(String name) {

    }

    public String getName() {
        return name;
    }

    public boolean requiresConcentration() {
        return requiresConcentration;
    }

    public SpellEffect getEffect() {
        return effect;
    }

    public void use(Entity caster, List<Entity> targets) {
        if (requiresConcentration) {
            caster.breakConcentration();
        }
        resolveTargets(caster, targets);
    }

    protected abstract void resolveTargets(Entity caster, List<Entity> targets);

    protected void applyEffect(Entity target) {
        if (effect != null) {
            target.applyEffect(effect);
        }
    }

    public boolean hasRecurringSave() {
        return recurringSave;
    }

    public AbilityTypes getSaveType() {
        return saveType;
    }
}
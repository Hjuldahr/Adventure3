package com.example.adventure.action;

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

    protected Effect effect;

    protected boolean requiresConcentration;

    public String getName() {
        return name;
    }

    public boolean requiresConcentration() {
        return requiresConcentration;
    }

    public Effect getEffect() {
        return effect;
    }

    public void use(Entity caster, List<Entity> targets) {

        if (requiresConcentration) {
            caster.setConcentratedSpell(this);
        }

        resolveTargets(caster, targets);
    }

    private void resolveTargets(Entity caster, List<Entity> targets) {

        switch (areaType) {

            case SELF -> applyToTarget(caster, caster);

            case SINGLE_TARGET ->
                    applyToTarget(caster, targets.getFirst());

            case MULTI_TARGET ->
                    targets.stream()
                            .limit(multiTargetLimit)
                            .forEach(t -> applyToTarget(caster, t));

            case AOE ->
                    targets.forEach(t -> applyToTarget(caster, t));
        }
    }

    protected abstract void applyToTarget(Entity caster, Entity target);

    protected void applyEffect(Entity target) {
        if (effect != null) {
            target.applyEffect(effect);
        }
    }

    public void concentrationBroken() {
        if (effect != null && requiresConcentration) {
            effect.removeAllEntities();
        }
    }
}
package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.libs.enums;

import static com.yor42.projectazure.libs.utils.MathUtil.rand;
import static com.yor42.projectazure.libs.utils.MathUtil.rollBooleanRNG;

public class AmmoProperties {

    private final float damage_rigging, damage_entity, damage_component, hitChance, minimum_damage_modifier;
    private final boolean shouldDamageMultipleComponant;
    enums.AmmoCategory category;

    public AmmoProperties(enums.AmmoCategory category, float damage_rigging, float damage_entity, float damage_component, float hitChance, float minimum_damage_modifier, boolean shouldDamageMultipleComponant){
        this.category = category;
        this.damage_rigging = damage_rigging;
        this.damage_entity = damage_entity;
        this.damage_component = damage_component;
        this.hitChance = hitChance;
        this.minimum_damage_modifier = minimum_damage_modifier;
        this.shouldDamageMultipleComponant = shouldDamageMultipleComponant;
    }

    public enums.AmmoCategory getCategory() {
        return this.category;
    }

    public float getDamage(enums.DamageType type) {
        switch (type){
            default:
                return this.damage_rigging*getDamageVariation();
            case ENTITY:
                return this.damage_entity*getDamageVariation();
            case COMPONENT:
                return this.damage_component*getDamageVariation();
        }
    }

    public float getMaxDamage(){
        return Math.max(Math.max(this.damage_component, this.damage_entity), this.damage_rigging)*getDamageVariation();
    }

    public boolean isHit(){
        return rollBooleanRNG(this.hitChance);
    }

    private float getDamageVariation(){
        return (rand.nextFloat()*(1-this.minimum_damage_modifier)+this.minimum_damage_modifier);
    }

    public boolean isExplosive(){
        return this.shouldDamageMultipleComponant;
    }

    public boolean isFiery(){
        return this.category.isFiery();
    }

    public float getRawComponentDamage() {
        return damage_component;
    }

    public float getRawEntityDamage() {
        return damage_entity;
    }

    public float getRawRiggingDamage() {
        return damage_rigging;
    }

    public float getRawhitChance() {
        return hitChance;
    }

    public float getRawDamageModifer() {
        return minimum_damage_modifier;
    }


}

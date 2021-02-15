package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.AmmoProperties;

public class ItemAmmo extends itemBaseTooltip{
    enums.AmmoCategory ammoCategory;
    private final float damage_rigging, damage_entity, damage_component, hitChance, minimum_damage_modifier;
    private boolean shouldDamageMultipleComponant, isIncendiary;

    public ItemAmmo(enums.AmmoCategory category, Properties ItemProperties){
        this(category, 0,0,0,0,0,category.ShouldDamageMultipleComponent(),category.isFiery(),ItemProperties);
    }
    public ItemAmmo(enums.AmmoCategory ammoType, float damage_rigging, float damage_entity, float damage_component, float hitChance, float minimum_damage_modifier, boolean shouldDamageMultipleComponant, boolean isIncendiary, Properties properties) {
        super(properties);
        this.ammoCategory = ammoType;
        this.damage_rigging = damage_rigging;
        this.damage_entity = damage_entity;
        this.damage_component = damage_component;
        this.hitChance = hitChance;
        this.minimum_damage_modifier = minimum_damage_modifier;

        this.shouldDamageMultipleComponant = shouldDamageMultipleComponant;
        this.isIncendiary = isIncendiary;
    }


    public AmmoProperties getAmmoProperty() {
        float damage_rigging = this.damage_rigging>0? this.damage_rigging:this.ammoCategory.getRawRiggingDamage();
        float damage_entity = this.damage_entity>0? this.damage_entity:this.ammoCategory.getRawEntityDamage();
        float damage_component = this.damage_component>0? this.damage_component:this.ammoCategory.getRawComponentDamage();
        float hitChance = this.minimum_damage_modifier>0? this.minimum_damage_modifier: this.ammoCategory.getRawhitChance();
        float minimum_damage_modifier = this.minimum_damage_modifier>0? this.minimum_damage_modifier:this.ammoCategory.getRawDamageModifer();

        return new AmmoProperties(this.ammoCategory, damage_rigging, damage_entity, damage_component, hitChance, minimum_damage_modifier, this.shouldDamageMultipleComponant, this.isIncendiary);
    }
}

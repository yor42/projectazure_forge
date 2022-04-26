package com.yor42.projectazure.gameobject.entity.companion.gunusers;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.Level;

public abstract class EntityGunUserBase extends AbstractEntityCompanion {

    protected EntityGunUserBase(EntityType<? extends TameableEntity> type, Level worldIn) {
        super(type, worldIn);
        this.AmmoStorage.setSize(8);
    }

}

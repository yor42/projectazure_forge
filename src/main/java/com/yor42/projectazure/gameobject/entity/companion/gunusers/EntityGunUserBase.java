package com.yor42.projectazure.gameobject.entity.companion.gunusers;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;

public abstract class EntityGunUserBase extends AbstractEntityCompanion {

    protected EntityGunUserBase(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.AmmoStorage.setSize(8);
    }

}

package com.yor42.projectazure.gameobject.entity.companion.gunusers;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;

public abstract class EntityGunUserBase extends AbstractEntityCompanion {

    protected EntityGunUserBase(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.AmmoStorage.setSize(8);
    }

}

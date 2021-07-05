package com.yor42.projectazure.gameobject.entity.companion.sworduser;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;

public abstract class AbstractSwordUserBase extends AbstractEntityCompanion {
    protected AbstractSwordUserBase(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    //Dont delete yet might handle skills and shit later

}

package com.yor42.projectazure.gameobject.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;

public class EntityKansenDestroyer extends EntityKansenBase {
    protected EntityKansenDestroyer(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setShipClass(shipClass.Destroyer);
    }
}

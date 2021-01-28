package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;

public class EntityKansenDestroyer extends EntityKansenBase {
    protected EntityKansenDestroyer(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setShipClass(enums.shipClass.Destroyer);
    }
}

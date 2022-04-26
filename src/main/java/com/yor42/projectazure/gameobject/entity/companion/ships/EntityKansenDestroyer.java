package com.yor42.projectazure.gameobject.entity.companion.ships;

import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.Level;

public abstract class EntityKansenDestroyer extends EntityKansenBase {
    protected EntityKansenDestroyer(EntityType<? extends TameableEntity> type, Level worldIn) {
        super(type, worldIn);
        this.setShipClass(enums.shipClass.Destroyer);
    }
}

package com.yor42.projectazure.gameobject.entity.companion.ships;

import com.yor42.projectazure.libs.enums;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;

public abstract class EntityKansenDestroyer extends EntityKansenBase {
    protected EntityKansenDestroyer(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.setShipClass(enums.shipClass.Destroyer);
    }
}

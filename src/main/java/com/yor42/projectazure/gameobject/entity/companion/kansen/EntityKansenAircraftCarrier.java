package com.yor42.projectazure.gameobject.entity.companion.kansen;

import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;

public abstract class EntityKansenAircraftCarrier extends EntityKansenBase {
    protected EntityKansenAircraftCarrier(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setShipClass(enums.shipClass.AircraftCarrier);
    }
}

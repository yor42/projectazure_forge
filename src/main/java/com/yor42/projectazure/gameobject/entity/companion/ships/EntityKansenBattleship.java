package com.yor42.projectazure.gameobject.entity.companion.ships;

import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.Level;

public abstract class EntityKansenBattleship extends EntityKansenBase{

    protected EntityKansenBattleship(EntityType<? extends TameableEntity> type, Level worldIn) {
        super(type, worldIn);
        this.setShipClass(enums.shipClass.Battleship);
    }
}

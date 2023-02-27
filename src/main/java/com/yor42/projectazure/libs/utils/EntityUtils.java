package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.hasPlanes;

public class EntityUtils {
    public static boolean EntityHasPlanes(EntityKansenBase entity){
        return hasPlanes(entity.getRigging());
    }

}

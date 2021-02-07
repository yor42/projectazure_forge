package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import com.yor42.projectazure.gameobject.capability.InventoryRiggingDefaultDD;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class CapabilityUtil {

    public static InventoryRiggingDefaultDD getRiggingInv(LivingEntity entity){
        ItemStack stack;
        if(entity instanceof EntityKansenBase){
            stack = ((EntityKansenBase) entity).getRigging();
        }
        else {
            stack = ((PlayerEntity) entity).getActiveItemStack();
        }

        return new InventoryRiggingDefaultDD(stack , entity);
    }

}

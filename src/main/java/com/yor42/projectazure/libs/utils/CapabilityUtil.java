package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.gameobject.containers.riggingcontainer.IRiggingContainerSupplier;
import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.InventoryRiggingDefaultDD;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

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

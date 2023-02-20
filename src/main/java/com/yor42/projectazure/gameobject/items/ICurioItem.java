package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.intermod.curios.client.ICurioRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public interface ICurioItem {
    default void curioTick(LivingEntity entity, int index, ItemStack stack){}

    default void curioOnEquip(LivingEntity wearer, int index, ItemStack prevstack){}
    @Nullable
    @OnlyIn(Dist.CLIENT)
    default ICurioRenderer getSlotRenderer(){
        return null;
    }
}

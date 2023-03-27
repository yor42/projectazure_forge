package com.yor42.projectazure.gameobject.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ICurioItem {
    default void curioTick(LivingEntity entity, int index, ItemStack stack){}

    default void curioOnEquip(LivingEntity wearer, int index, ItemStack prevstack){}
}

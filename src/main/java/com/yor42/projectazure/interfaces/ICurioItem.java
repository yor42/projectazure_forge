package com.yor42.projectazure.interfaces;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public interface ICurioItem {
    default void curioTick(LivingEntity entity, int index, ItemStack stack){}

    default void curioOnEquip(LivingEntity wearer, int index, ItemStack prevstack){}

    default void curioOnUnEquip(SlotContext context, ItemStack prevstack){}
}

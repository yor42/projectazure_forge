package com.yor42.projectazure.gameobject.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import javax.annotation.Nullable;

public interface ICurioItem {
    default void curioTick(LivingEntity entity, int index, ItemStack stack){}

    default void curioOnEquip(LivingEntity wearer, int index, ItemStack prevstack){}
}

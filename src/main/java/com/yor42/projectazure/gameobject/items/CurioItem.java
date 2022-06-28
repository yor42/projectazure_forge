package com.yor42.projectazure.gameobject.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CurioItem extends Item {
    public CurioItem(Properties properties) {
        super(properties);
    }

    public void curioTick(LivingEntity entity, ItemStack stack){}
}

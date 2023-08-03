package com.yor42.projectazure.interfaces;

import net.minecraft.world.item.ItemStack;

public interface ISecondaryBar {

    default boolean isSecondaryBarVisible(ItemStack stack){
        return false;
    }

    default int getSecondaryBarColor(ItemStack stack){
        return 0;
    }
    default int getSecondaryBarWidth(ItemStack stack){
        return 0;
    }
}

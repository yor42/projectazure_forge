package com.yor42.projectazure.interfaces;

import net.minecraft.world.item.ItemStack;

public interface IItemDestroyable {

    int getMaxHP();

    default int getRepairAmount(ItemStack candidateItem){
        return 0;
    }

}

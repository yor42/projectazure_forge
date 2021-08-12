package com.yor42.projectazure.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

public interface IRiggingContainerSupplier {

    void saveAll();

    void saveEquipments(CompoundNBT nbt);
    void loadEquipments(CompoundNBT nbt);

    ItemStackHandler getEquipments();

    ItemStack getRigging();

    CompoundNBT getNBT(ItemStack stack);
}

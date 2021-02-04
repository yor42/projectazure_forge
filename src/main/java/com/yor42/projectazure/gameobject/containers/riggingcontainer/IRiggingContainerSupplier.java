package com.yor42.projectazure.gameobject.containers.riggingcontainer;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

public interface IRiggingContainerSupplier {

    void saveAll();

    void saveEquipments(CompoundNBT nbt);
    void loadEquipments(CompoundNBT nbt);

    ItemStackHandler getEquipments();

    CompoundNBT getNBT(ItemStack stack);

}

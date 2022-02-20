package com.yor42.projectazure.gameobject.capability;

import com.yor42.projectazure.gameobject.storages.CustomEnergyStorageItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemPowerCapabilityProvider implements ICapabilityProvider {

    protected ItemStack Stack;
    private int EnergyCapacity;

    public ItemPowerCapabilityProvider(ItemStack stack, int capacity){
        this.Stack = stack;
        this.EnergyCapacity = capacity;
    }



    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(this.Stack != null) {
            if (cap == CapabilityEnergy.ENERGY) {
                final LazyOptional<CustomEnergyStorageItem> EnergyStorage = LazyOptional.of(() -> new CustomEnergyStorageItem(this.Stack, this.EnergyCapacity));
                EnergyStorage.resolve();
                return EnergyStorage.cast();
            }
        }
        return LazyOptional.empty();
    }
}
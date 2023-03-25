package com.yor42.projectazure.gameobject.capability;

import com.yor42.projectazure.gameobject.storages.CustomEnergyStorageItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemPowerCapabilityProvider implements ICapabilityProvider {

    protected ItemStack Stack;
    private final int EnergyCapacity, maxin, maxout;

    public ItemPowerCapabilityProvider(ItemStack stack, int capacity){
        this(stack, capacity, capacity, capacity);
    }

    public ItemPowerCapabilityProvider(ItemStack stack, int capacity, int maxin, int maxout){
        this.Stack = stack;
        this.EnergyCapacity = capacity;
        this.maxin = maxin;
        this.maxout = maxout;
    }



    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(this.Stack != null) {
            if (cap == CapabilityEnergy.ENERGY) {
                final LazyOptional<CustomEnergyStorageItem> EnergyStorage = LazyOptional.of(() -> new CustomEnergyStorageItem(this.Stack, this.EnergyCapacity, this.maxin, this.maxout));
                EnergyStorage.resolve();
                return EnergyStorage.cast();
            }
        }
        return LazyOptional.empty();
    }
}

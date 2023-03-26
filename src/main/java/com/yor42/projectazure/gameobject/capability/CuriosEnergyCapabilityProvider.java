package com.yor42.projectazure.gameobject.capability;

import com.yor42.projectazure.gameobject.storages.CustomEnergyStorageItem;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CuriosEnergyCapabilityProvider extends CuriosCapabilityProvider {

    private final int energycap;

    public CuriosEnergyCapabilityProvider(ItemStack stack, int energysize) {
        super(stack);
        this.energycap = energysize;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            final LazyOptional<CustomEnergyStorageItem> EnergyStorage = LazyOptional.of(() -> new CustomEnergyStorageItem(this.stack, this.energycap));
            EnergyStorage.resolve();
            return EnergyStorage.cast();
        }
        return super.getCapability(cap, side);
    }
}

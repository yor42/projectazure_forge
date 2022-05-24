package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.storages.CustomEnergyStorage;
import com.yor42.projectazure.setup.register.registerTE;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

public class TileEntityEnergyHatch extends TileEntity {
    CustomEnergyStorage energy = new CustomEnergyStorage(10000);

    public TileEntityEnergyHatch() {
        super(registerTE.ENERGY_HATCH.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if(cap == CapabilityEnergy.ENERGY){
            return LazyOptional.of(()->this.energy).cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("storage", this.energy.serializeNBT());
        return super.save(compound);
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        this.energy.deserializeNBT(p_230337_2_.getCompound("storage"));
        super.load(p_230337_1_, p_230337_2_);
    }
}

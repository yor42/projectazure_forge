package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.setup.register.registerTE;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

public class TileEntityFluidHatch extends TileEntity {

    FluidTank tank = new FluidTank(5000);

    public TileEntityFluidHatch() {
        super(registerTE.FLUID_HATCH.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if(cap == FLUID_HANDLER_CAPABILITY){
            return LazyOptional.of(()->this.tank).cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("tank", this.tank.writeToNBT(new CompoundNBT()));
        return super.save(compound);
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        this.tank.readFromNBT(p_230337_2_.getCompound("tank"));
        super.load(p_230337_1_, p_230337_2_);
    }
}

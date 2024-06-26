package com.yor42.projectazure.gameobject.capability.multiinv;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.libs.enums;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.FluidPredicates.FuelOilPredicate;

public class RiggingItemCapabilityProvider implements ICapabilityProvider {

    protected ItemStack stack;
    protected final LivingEntity entity;
    private int fuelTankCapacity;
    private ImmutableMap<enums.SLOTTYPE, MultiInvStackHandler> inventories;

    public RiggingItemCapabilityProvider(ItemStack stack, @Nullable LivingEntity entity, int FuelTankCapacity, ImmutableMap<enums.SLOTTYPE, MultiInvStackHandler> inventories){
        this.fuelTankCapacity = FuelTankCapacity;
        this.stack = stack;
        this.entity = entity;
        this.inventories = inventories;
    }

    private final LazyOptional<IFluidHandlerItem> fuelTank = LazyOptional.of(() -> new FluidHandlerItemStack(stack, this.fuelTankCapacity) {
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return FuelOilPredicate.test(fluid);
        }
    });

    private final LazyOptional<IMultiInventory> multiInventory = LazyOptional.of(() -> new IMultiInventory.Impl(this.inventories));

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
            return fuelTank.cast();
        } else if (cap == CapabilityMultiInventory.MULTI_INVENTORY_CAPABILITY) {
            return multiInventory.cast();
        }
        return LazyOptional.empty();
    }
}

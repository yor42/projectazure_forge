package com.yor42.projectazure.gameobject.capability;

import com.yor42.projectazure.intermod.curios.PACuriosCap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CuriosCapabilityProvider implements ICapabilityProvider {

    protected final ItemStack stack;

    public CuriosCapabilityProvider(ItemStack stack){
        this.stack = stack;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return top.theillusivec4.curios.api.CuriosCapability.ITEM.orEmpty(cap, LazyOptional.of(()->new PACuriosCap(this.stack)));
    }
}

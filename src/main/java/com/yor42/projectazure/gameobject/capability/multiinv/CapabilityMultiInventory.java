package com.yor42.projectazure.gameobject.capability.multiinv;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.apache.commons.lang3.NotImplementedException;

import javax.annotation.Nullable;

public class CapabilityMultiInventory {
    @CapabilityInject(IMultiInventory.class)
    public static Capability<IMultiInventory> MULTI_INVENTORY_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IMultiInventory.class, new MultiInventoryStorage<>(), IMultiInventory.Impl::new);
    }

    public static class MultiInventoryStorage<T extends IMultiInventory> implements Capability.IStorage<T> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
            throw new NotImplementedException();
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
            throw new NotImplementedException();
        }
    }
}
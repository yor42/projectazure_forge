package com.yor42.projectazure.gameobject.capability.multiinv;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.apache.commons.lang3.NotImplementedException;

import javax.annotation.Nullable;

public class CapabilityMultiInventory {
    public static Capability<IMultiInventory> MULTI_INVENTORY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
}

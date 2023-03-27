package com.yor42.projectazure.gameobject.capability.multiinv;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class CapabilityMultiInventory {
    public static Capability<IMultiInventory> MULTI_INVENTORY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
}

package com.yor42.projectazure.gameobject.capability.multiinv;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class MultiInvStackHandler extends ItemStackHandler {
    private final ItemStack container;
    private final String id;

    public MultiInvStackHandler(ItemStack container, String id, int size) {
        super(size);
        this.container = container;
        this.id = id;

        if (container.getOrCreateTag().contains("Inventory_" + id)) {
            this.deserializeNBT(container.getOrCreateTag().getCompound("Inventory_" + id));
        }
    }

    @Override
    protected void onContentsChanged(int slot) {
        container.getOrCreateTag().put("Inventory_" + this.id, this.serializeNBT());
    }
}

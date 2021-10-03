package com.yor42.projectazure.gameobject.capability.multiinv;

import net.minecraft.item.ItemStack;

public class MultiInvUtil {
    public static IMultiInventory getCap(ItemStack stack) {
        return stack.getCapability(CapabilityMultiInventory.MULTI_INVENTORY_CAPABILITY).orElseThrow(() -> new RuntimeException("MultiInventory capability not present on stack"));
    }

    public static int getSlotCount(IMultiInventory inventories, int[] indices) {
        int sum = 0;
        for (int index : indices) {
            sum += inventories.getInventory(index).getSlots();
        }
        return sum;
    }

    public static ItemStack getStack(IMultiInventory inventories, int slot) {
        int inventoryCount = inventories.getInventoryCount();
        int currentInv = 0;

        while (currentInv < inventoryCount && slot >= inventories.getInventory(currentInv).getSlots())
        {
            slot -= inventories.getInventory(currentInv).getSlots();
        }

        if (slot < inventories.getInventory(currentInv).getSlots())
        {
            return inventories.getInventory(currentInv).getStackInSlot(slot);
        }

        throw new IllegalArgumentException(String.format("Argument 'slot' out of bounds, index = %d but last inventory only has %d slots.", slot, inventories.getInventory(currentInv).getSlots()));
    }
}

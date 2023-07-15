package com.yor42.projectazure.gameobject.capability.multiinv;

import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.world.item.ItemStack;

public class MultiInvUtil {
    public static IMultiInventory getCap(ItemStack stack) {

        return stack.getCapability(CapabilityMultiInventory.MULTI_INVENTORY_CAPABILITY).orElseThrow(() -> new RuntimeException("MultiInventory capability not present on stack"));
    }


    public static int getSlotCount(IMultiInventory inventories, enums.SLOTTYPE[] indices) {
        int sum = 0;
        for (enums.SLOTTYPE index : indices) {
            sum += inventories.getInventory(index).getSlots();
        }
        return sum;
    }

    public static ItemStack getRandomStack(IMultiInventory inventories){
        enums.SLOTTYPE slottype = inventories.getAllKeys().get(MathUtil.getRand().nextInt(inventories.getAllKeys().size()));
        MultiInvStackHandler handler = inventories.getInventory(slottype);
        return handler.getStackInSlot(MathUtil.getRand().nextInt(handler.getSlots()));
    }

}

package com.yor42.projectazure.gameobject.containers.slots;

import com.yor42.projectazure.gameobject.items.ItemEquipmentBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class slotEquipment extends SlotItemHandler {

    private enums.SLOTTYPE slottype;

    public slotEquipment(IItemHandler itemHandler, int index, int xPosition, int yPosition, enums.SLOTTYPE type) {
        super(itemHandler, index, xPosition, yPosition);
        this.slottype = type;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        if(stack.getItem() instanceof ItemEquipmentBase) {
            return ((ItemEquipmentBase) stack.getItem()).getSlot() == this.slottype;
        }
        else
            return false;
    }
}

package com.yor42.projectazure.gameobject.capability.multiinv;

import com.yor42.projectazure.libs.enums;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class MultiInvEquipmentHandler extends MultiInvStackHandler{
    @Nonnull
    private final enums.SLOTTYPE slotType;
    public MultiInvEquipmentHandler(ItemStack container, String id, int size, @Nonnull enums.SLOTTYPE EquipmentType) {
        super(container, id, size);
        this.slotType = EquipmentType;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return this.slotType.testPredicate(stack);
    }
}

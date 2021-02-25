package com.yor42.projectazure.gameobject.containers.slots;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotRigging extends SlotItemHandler {

    private EntityKansenBase hostEntity;

    public SlotRigging(IItemHandler itemHandler, int index, int xPosition, int yPosition, EntityKansenBase hostEntity) {
        super(itemHandler, index, xPosition, yPosition);
        this.hostEntity = hostEntity;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        if(stack.getItem() instanceof ItemRiggingBase){
            return ((ItemRiggingBase) stack.getItem()).getValidclass() == hostEntity.getShipClass();
        }
        return false;
    }
}

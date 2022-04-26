package com.yor42.projectazure.gameobject.containers.slots;

import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import net.minecraft.entity.player.Inventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;

public class slotInventory extends Slot {

    private final Inventory handler;
    private final int index;

    public slotInventory(Inventory itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.handler = itemHandler;
        this.index = index;
    }

    @Override
    public boolean mayPickup(PlayerEntity playerIn) {
        if(this.getHandler().getItem(this.index).getItem() instanceof ItemRiggingBase){
            return false;
        }
        return super.mayPickup(playerIn);
    }

    public Inventory getHandler() {
        return this.handler;
    }
}

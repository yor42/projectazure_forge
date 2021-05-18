package com.yor42.projectazure.gameobject.containers.machine;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.gameobject.containers.slots.ResultSlotStackHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.setup.register.registerManager.METAL_PRESS_CONTAINER_TYPE;

public class ContainerMetalPress extends Container {

    private final IIntArray field;

    public ContainerMetalPress(int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(id, inventory, new ItemStackHandler(3), new IntArray(4));
    }

    public ContainerMetalPress(int id, PlayerInventory inventory, ItemStackHandler Inventory, IIntArray field) {
        super(METAL_PRESS_CONTAINER_TYPE, id);

        this.addSlot(new SlotItemHandler(Inventory, 0, 41, 35));
        this.addSlot(new SlotMold(Inventory, 1, 75,35));
        this.addSlot(new ResultSlotStackHandler(inventory.player, Inventory, 2, 116, 35, false));

        this.field = field;

        trackIntArray(this.field);


        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }

    public IIntArray getField() {
        return this.field;
    }

    public int getStoredPowerScaled(int pixels){

        int currentpower = this.field.get(2);
        int maxpower = this.field.get(3);
        return currentpower != 0 && maxpower != 0? currentpower*pixels/maxpower:0;
    }

    public int getprogressScaled(int pixels){
        int i = this.field.get(0);
        int j = this.field.get(1);
        double k = (double) i / j;
        return (int)(i != 0 ? k * pixels : 0);
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1 && index != 0) {
                if (itemstack1.getItem().isIn(ModTags.Items.EXTRUSION_MOLD)) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 30) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                    else if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
    //Why do I have to do this all over again forge :kekw:

    private static class SlotMold extends SlotItemHandler{

        public SlotMold(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return stack.getItem().isIn(ModTags.Items.EXTRUSION_MOLD);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }
}

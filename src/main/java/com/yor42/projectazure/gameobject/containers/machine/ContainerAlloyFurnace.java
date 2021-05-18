package com.yor42.projectazure.gameobject.containers.machine;

import com.yor42.projectazure.gameobject.containers.slots.FuelSlotItemhandler;
import com.yor42.projectazure.gameobject.containers.slots.ResultSlotStackHandler;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAlloyFurnace extends Container {

    private final IIntArray field;

    public ContainerAlloyFurnace(int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(id, inventory, new ItemStackHandler(4), new IntArray(4));
    }

    public ContainerAlloyFurnace(int id, PlayerInventory inventory, ItemStackHandler machineInventory, IIntArray machineInfo){
        super(registerManager.CONTAINER_ALLOY_FURNACE_CONTAINER_TYPE, id);
        this.field = machineInfo;
        trackIntArray(this.field);

        this.addSlot(new SlotItemHandler(machineInventory, 0, 47, 17));
        this.addSlot(new SlotItemHandler(machineInventory, 1, 65,17));
        this.addSlot(new FuelSlotItemhandler(machineInventory, 2, 56,53));
        this.addSlot(new ResultSlotStackHandler(inventory.player, machineInventory, 3, 116, 35, true));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }
    public int getCookProgressionScaled(int pixel) {
        int i = this.field.get(2);
        int j = this.field.get(3);
        return j != 0 && i != 0 ? i * pixel / j : 0;
    }

    public boolean isBurning(){
        return this.field.get(1)>0;
    }

    public int getBurnLeftScaled(int pixel) {
        int i = this.field.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.field.get(0) * pixel / i;
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 3) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1 && index != 0 && index != 2) {
                if (ForgeHooks.getBurnTime(itemstack1)>0) {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < 30) {
                    if (!this.mergeItemStack(itemstack1, 0, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                    else if (!this.mergeItemStack(itemstack1, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 4, 40, false)) {
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
}

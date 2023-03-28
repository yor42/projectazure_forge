package com.yor42.projectazure.gameobject.containers.machine;

import com.yor42.projectazure.gameobject.containers.slots.FuelSlotItemhandler;
import com.yor42.projectazure.gameobject.containers.slots.ResultSlotStackHandler;
import com.yor42.projectazure.setup.register.RegisterContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class ContainerAlloyFurnace extends AbstractContainerMenu {

    @Nullable
    private ContainerData field;

    public ContainerAlloyFurnace(int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(id, inventory, new ItemStackHandler(4), new ContainerData() {
            @Override
            public int get(int pIndex) {
                return 0;
            }

            @Override
            public void set(int pIndex, int pValue) {

            }

            @Override
            public int getCount() {
                return 4;
            }
        });
    }

    public ContainerAlloyFurnace(int id, Inventory inventory, ItemStackHandler machineInventory, ContainerData machineInfo){
        super(RegisterContainer.ALLOY_FURNACE_CONTAINER.get(), id);
        this.field = machineInfo;
        addDataSlots(this.field);

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

    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 3) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index != 1 && index != 0 && index != 2) {
                if (ForgeHooks.getBurnTime(itemstack1, null)>0) {
                    if (!this.moveItemStackTo(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < 30) {
                    if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                    else if (!this.moveItemStackTo(itemstack1, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 4, 40, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }
}

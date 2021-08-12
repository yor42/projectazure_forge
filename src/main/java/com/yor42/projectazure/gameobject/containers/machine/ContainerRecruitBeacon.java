package com.yor42.projectazure.gameobject.containers.machine;

import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.setup.register.registerManager.RECRUIT_BEACON_CONTAINER_TYPE;

public class ContainerRecruitBeacon extends Container {

    private final IIntArray field;

    public ContainerRecruitBeacon(int id, PlayerInventory inventory, PacketBuffer buffer) {
this(id, inventory, new ItemStackHandler(5), new IIntArray() {

    final int[] values = buffer.readVarIntArray();

    @Override
    public int get(int index) {
        return values[index];
    }

    @Override
    public void set(int index, int value) {
        values[index] = value;
    }

    @Override
    public int size() {
        return values.length;
    }
});
    }

    public ContainerRecruitBeacon(int id, PlayerInventory inventory, ItemStackHandler Inventory, IIntArray field) {
        super(RECRUIT_BEACON_CONTAINER_TYPE, id);
        this.field = field;
        trackIntArray(this.field);
        this.addSlot(new SlotItemHandler(Inventory, 0, 19, 10){
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return stack.getItem() == registerItems.HEADHUNTING_PCB.get();
            }
        });
        for(int i=0; i<2; i++){
            this.addSlot(new SlotItemHandler(Inventory, 1+i, 10+18*i, 33){
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return stack.getItem() == registerItems.ORUNDUM.get();
                }
            });
        }
        for(int i=0; i<2; i++){
            this.addSlot(new SlotItemHandler(Inventory, 3+i, 10+18*i, 56){
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return stack.getItem() == Items.GOLD_INGOT;
                }
            });
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }

    public int getRemainingTick(){
        return this.getField().get(1)-this.getField().get(0);
    }

    public BlockPos getBlockPos(){
        return new BlockPos(this.field.get(4),this.field.get(5),this.field.get(6));
    }

    public IIntArray getField() {
        return this.field;
    }

    public int getStoredPowerScaled(int pixels){

        int currentpower = this.field.get(2);
        int maxpower = this.field.get(3);
        return currentpower != 0 && maxpower != 0? currentpower*pixels/maxpower:0;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index <5) {
                if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else {
                if (itemstack1.getItem() == registerItems.HEADHUNTING_PCB.get()) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.getItem() == registerItems.ORUNDUM.get()) {
                    if (!this.mergeItemStack(itemstack1, 1, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.getItem().isIn(Tags.Items.INGOTS_GOLD)) {
                    if (!this.mergeItemStack(itemstack1, 3, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                }
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

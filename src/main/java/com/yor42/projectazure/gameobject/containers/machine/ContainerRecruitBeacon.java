package com.yor42.projectazure.gameobject.containers.machine;

import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.setup.register.RegisterContainer.RECRUIT_BEACON_CONTAINER;

public class ContainerRecruitBeacon extends AbstractContainerMenu {

    @Nullable
    private ContainerData field;

    public ContainerRecruitBeacon(int id, Inventory inventory) {
        super(RECRUIT_BEACON_CONTAINER.get(), id);
    }

    public ContainerRecruitBeacon(int id, Inventory inventory, ItemStackHandler Inventory, ContainerData field) {
        super(RECRUIT_BEACON_CONTAINER.get(), id);
        this.field = field;
        addDataSlots(this.field);
        this.addSlot(new SlotItemHandler(Inventory, 0, 19, 10){
            @Override
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return stack.getItem() == RegisterItems.HEADHUNTING_PCB.get();
            }
        });
        for(int i=0; i<2; i++){
            this.addSlot(new SlotItemHandler(Inventory, 1+i, 10+18*i, 33){
                @Override
                public boolean mayPlace(@Nonnull ItemStack stack) {
                    return stack.getItem() == RegisterItems.ORUNDUM.get();
                }
            });
        }
        for(int i=0; i<2; i++){
            this.addSlot(new SlotItemHandler(Inventory, 3+i, 10+18*i, 56){
                @Override
                public boolean mayPlace(@Nonnull ItemStack stack) {
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

    public ContainerData getField() {
        return this.field;
    }

    public int getStoredPowerScaled(int pixels){

        int currentpower = this.field.get(2);
        int maxpower = this.field.get(3);
        return currentpower != 0 && maxpower != 0? currentpower*pixels/maxpower:0;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index <5) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else {
                if (itemstack1.is(RegisterItems.HEADHUNTING_PCB.get())) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.is(RegisterItems.ORUNDUM.get())) {
                    if (!this.moveItemStackTo(itemstack1, 1, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.is(Tags.Items.INGOTS_GOLD)) {
                    if (!this.moveItemStackTo(itemstack1, 3, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                }
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

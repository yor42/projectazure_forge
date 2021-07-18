package com.yor42.projectazure.gameobject.containers.machine;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.setup.register.registerItems;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.setup.register.registerManager.DRYDOCK_CONTAINER_TYPE;
import static com.yor42.projectazure.setup.register.registerManager.METAL_PRESS_CONTAINER_TYPE;
import static com.yor42.projectazure.setup.register.registerTE.DRYDOCK;

public class ContainerDryDock extends Container {

    private final IIntArray field;

    public ContainerDryDock(int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(id, inventory, new ItemStackHandler(9), new IIntArray() {

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

    public ContainerDryDock(int id, PlayerInventory inventory, ItemStackHandler Inventory, IIntArray field){
        super(DRYDOCK_CONTAINER_TYPE, id);
        this.field = field;
        trackIntArray(this.field);

        this.addSlot(new SlotItemHandler(Inventory, 0, 19, 10){
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return stack.getItem() == registerItems.WISDOM_CUBE.get();
            }
        });

        for(int i=0; i<2; i++){
            this.addSlot(new SlotItemHandler(Inventory, 1+i, 10+18*i, 33){
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return stack.getItem().isIn(ModTags.Items.INGOT_ALUMINIUM);
                }
            });
        }
        for(int i=0; i<2; i++){
            this.addSlot(new SlotItemHandler(Inventory, 3+i, 10+18*i, 56){
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return stack.getItem().isIn(ModTags.Items.INGOT_STEEL);
                }
            });
        }
        for(int i=0; i<2; i++){
            this.addSlot(new SlotItemHandler(Inventory, 5+i, 115+18*i, 33){
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return stack.getItem().isIn(ModTags.Items.INGOT_COPPER);
                }
            });
        }
        for(int i=0; i<2; i++){
            this.addSlot(new SlotItemHandler(Inventory, 7+i, 115+18*i, 56){
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return stack.getItem().isIn(ModTags.Items.INGOT_ZINC);
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
    @MethodsReturnNonnullByDefault
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index <9) {
                if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else {
                if (itemstack1.getItem() == registerItems.WISDOM_CUBE.get()) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.getItem().isIn(ModTags.Items.INGOT_ALUMINIUM)) {
                    if (!this.mergeItemStack(itemstack1, 1, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.getItem().isIn(ModTags.Items.INGOT_STEEL)) {
                    if (!this.mergeItemStack(itemstack1, 3, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.getItem().isIn(ModTags.Items.INGOT_COPPER)) {
                    if (!this.mergeItemStack(itemstack1, 5, 7, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.getItem().isIn(ModTags.Items.INGOT_ZINC)) {
                    if (!this.mergeItemStack(itemstack1, 7, 9, false)) {
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
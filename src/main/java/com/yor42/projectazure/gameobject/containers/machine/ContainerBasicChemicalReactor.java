package com.yor42.projectazure.gameobject.containers.machine;

import com.yor42.projectazure.setup.register.RegisterContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.ContainerData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerBasicChemicalReactor extends AbstractContainerMenu {

    private final ContainerData field;
    private final FluidStack outputtank;

    public ContainerBasicChemicalReactor(int id, Inventory inventory, FriendlyByteBuf buffer){
        this(id, inventory, new ItemStackHandler(3), new ContainerData() {

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
            public int getCount() {
                return values.length;
            }
        }, buffer.readFluidStack());
    }

    public ContainerBasicChemicalReactor(int id, Inventory inventory, ItemStackHandler itemStackHandler, ContainerData fields, FluidStack outputtank) {
        super(RegisterContainer.BASICCCHEMICALREACTOR.get(), id);

        this.field = fields;
        this.outputtank = outputtank;


        this.addSlot(new SlotItemHandler(itemStackHandler, 0, 44,35));

        this.addSlot(new ContainerBasicRefinery.BucketInSlot(itemStackHandler, 1, 116, 7));
        this.addSlot(new ContainerBasicRefinery.BucketOutSlot(itemStackHandler, 2, 116, 63));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

        this.addDataSlots(this.field);
    }

    @Override
    public boolean stillValid(@Nonnull Player p_75145_1_) {
        return true;
    }

    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        ItemStack CopyofStackinSlot = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack StackinSlot = slot.getItem();
            CopyofStackinSlot = StackinSlot.copy();
            //Bucket outs
            if (index < 3) {
                if (!this.moveItemStackTo(StackinSlot, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }

            } else {
                if (FluidUtil.getFluidHandler(StackinSlot).isPresent()) {
                    if (!this.moveItemStackTo(StackinSlot, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (!this.moveItemStackTo(StackinSlot, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (StackinSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (StackinSlot.getCount() == CopyofStackinSlot.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, StackinSlot);
        }

        return CopyofStackinSlot;
    }

    public FluidStack getOutputtank() {
        return outputtank;
    }

    public int getOutputTankCapacity(){
        return this.field.get(5);
    }
    public int getOutputTankAmount(){
        return this.field.get(4);
    }

    public int getStoredPowerScaled(int pixels){

        int currentpower = this.field.get(2);
        int maxpower = this.field.get(3);
        return currentpower != 0 && maxpower != 0? currentpower*pixels/maxpower:0;
    }
}

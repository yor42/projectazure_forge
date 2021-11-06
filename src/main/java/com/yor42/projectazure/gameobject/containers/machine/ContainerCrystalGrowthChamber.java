package com.yor42.projectazure.gameobject.containers.machine;

import com.yor42.projectazure.gameobject.items.ItemResource;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.AbstractFurnaceContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.setup.register.registerManager.GROWTH_CHAMBER_CONTAINER_TYPE;

public class ContainerCrystalGrowthChamber extends Container {

    private final IIntArray field;
    private final FluidStack waterTank, SolutionTank;

    public ContainerCrystalGrowthChamber(int id, PlayerInventory inventory, PacketBuffer buffer){
        this(id, inventory, new ItemStackHandler(7), new IIntArray() {

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
        }, buffer.readFluidStack(), buffer.readFluidStack());
    }

    public ContainerCrystalGrowthChamber(int id, PlayerInventory inventory, ItemStackHandler itemStackHandler, IIntArray field, FluidStack waterTank, FluidStack solutionTank) {
        super(GROWTH_CHAMBER_CONTAINER_TYPE, id);
        this.field = field;
        this.waterTank = waterTank;
        this.SolutionTank = solutionTank;

        this.addSlot(new SlotItemHandler(itemStackHandler, 0, 58,6));

        for(int i=0;i<3;i++){
            this.addSlot(new SlotItemHandler(itemStackHandler, i+1, 31,17+18*i));
        }

        this.addSlot(new SlotItemHandler(itemStackHandler, 4, 116,35));

        this.addSlot(new SlotItemHandler(itemStackHandler, 5, 12,6));
        this.addSlot(new SlotItemHandler(itemStackHandler, 6, 12,64){
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return false;
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

        this.trackIntArray(this.field);
    }

    public FluidStack getWaterTank() {
        return waterTank;
    }

    public FluidStack getSolutionTank() {
        return SolutionTank;
    }

    public int getWaterTankCapacity(){
        return this.field.get(3);
    }
    public int getWaterTankAmount(){
        return this.field.get(2);
    }
    public int getSolutionTankCapacity(){
        return this.field.get(5);
    }
    public int getSolutionTankAmount(){
        return this.field.get(4);
    }
    public int getprogressScaled(int pixels){

        int progress = this.field.get(0);
        int maxProgress = this.field.get(1);

        float length = (float)progress/maxProgress;
        return (int)(length*pixels);
    }
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        ItemStack itemstack1 = slot.getStack();
        if (slot.getHasStack()) {
            itemstack = itemstack1.copy();
            if (index < 7) {
                if (!this.mergeItemStack(itemstack1, 8, 39, false)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else {
                if (FluidUtil.getFluidHandler(itemstack1).isPresent()) {
                    if (!this.mergeItemStack(itemstack1, 5, 6, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.getItem() instanceof ItemResource && ((ItemResource) itemstack1.getItem()).getResourceType() == enums.ResourceType.DUST) {
                    if (!this.mergeItemStack(itemstack1, 1, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
                else {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
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
}

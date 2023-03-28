package com.yor42.projectazure.gameobject.containers.machine;

import com.yor42.projectazure.gameobject.items.ItemResource;
import com.yor42.projectazure.libs.enums;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.setup.register.RegisterContainer.GROWTH_CHAMBER_CONTAINER;

public class ContainerCrystalGrowthChamber extends AbstractContainerMenu {

    @Nullable
    private final ContainerData field;
    @Nullable
    private final FluidStack waterTank;
    @Nullable
    private final FluidStack SolutionTank;

    public ContainerCrystalGrowthChamber(int id, Inventory inventory, ItemStackHandler itemStackHandler, ContainerData field, FluidStack waterTank, FluidStack solutionTank) {
        super(GROWTH_CHAMBER_CONTAINER.get(), id);
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
            public boolean mayPlace(@Nonnull ItemStack stack) {
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

        this.addDataSlots(this.field);
    }

    public ContainerCrystalGrowthChamber(int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(id, inventory, new ItemStackHandler(7), new ContainerData() {

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
        }, buffer.readFluidStack(), buffer.readFluidStack());
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
    public boolean stillValid(Player playerIn) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        ItemStack itemstack1 = slot.getItem();
        if (slot.hasItem()) {
            itemstack = itemstack1.copy();
            if (index < 7) {
                if (!this.moveItemStackTo(itemstack1, 8, 39, false)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else {
                if (FluidUtil.getFluidHandler(itemstack1).isPresent()) {
                    if (!this.moveItemStackTo(itemstack1, 5, 6, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.getItem() instanceof ItemResource && ((ItemResource) itemstack1.getItem()).getResourceType() == enums.ResourceType.DUST) {
                    if (!this.moveItemStackTo(itemstack1, 1, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
                else {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
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
}

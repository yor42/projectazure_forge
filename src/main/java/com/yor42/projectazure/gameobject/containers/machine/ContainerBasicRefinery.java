package com.yor42.projectazure.gameobject.containers.machine;

import com.yor42.projectazure.setup.register.registerFluids;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.setup.register.registerFluids.*;
import static com.yor42.projectazure.setup.register.registerManager.BASIC_REFINERY_CONTAINER_TYPE;

public class ContainerBasicRefinery extends Container {

    private final IIntArray field;

    public ContainerBasicRefinery(int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(id, inventory, new ItemStackHandler(10), new IIntArray() {

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

    public ContainerBasicRefinery(int id, PlayerInventory inventory, ItemStackHandler Inventory, IIntArray field) {
        super(BASIC_REFINERY_CONTAINER_TYPE, id);
        this.field = field;

        this.addSlot(new FuelSlot(Inventory, 9, 37, 63));
        this.addSlot(new SlotItemHandler(Inventory, 8, 142, 35){
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

        this.addSlot(new BucketInSlot(Inventory, 0, 8, 7, registerFluids.CRUDE_OIL_SOURCE));
        this.addSlot(new BucketOutSlot(Inventory, 1, 8, 63));

        Fluid[] fluid = {GASOLINE_SOURCE, DIESEL_SOURCE, FUEL_OIL_SOURCE};
        for(int i=0; i<3; i++){
            int xval = 67+(23*i);
            this.addSlot(new BucketInSlot(Inventory, 2+(i*2), xval, 7, fluid[i]));
            this.addSlot(new BucketOutSlot(Inventory, 3+(i*2), xval, 63));
        }


        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

        this.trackIntArray(this.field);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    private static class FuelSlot extends SlotItemHandler{


        public FuelSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return ForgeHooks.getBurnTime(stack, IRecipeType.SMELTING)>0;
        }
    }

    private static class BucketInSlot extends SlotItemHandler{

        private final Fluid fluid;

        public BucketInSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Fluid fluid) {
            super(itemHandler, index, xPosition, yPosition);
            this.fluid = fluid;
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return FluidUtil.getFluidContained(stack).isPresent() && FluidUtil.getFluidContained(stack).get().getFluid() == this.fluid;
        }
    }

    private static class BucketOutSlot extends SlotItemHandler{

        public BucketOutSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return false;
        }
    }
}

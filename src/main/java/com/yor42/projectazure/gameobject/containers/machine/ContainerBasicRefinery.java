package com.yor42.projectazure.gameobject.containers.machine;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.setup.register.registerFluids;
import net.minecraft.network.FriendlyByteBuf ;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.setup.register.registerManager.BASIC_REFINERY_CONTAINER_TYPE;
import static net.minecraft.world.item.crafting.RecipeType.SMELTING;

public class ContainerBasicRefinery extends AbstractContainerMenu {

    private final ContainerData field;

    public final FluidStack crudeoilstack, gasolinestack, dieselstack, fueloilstack;

    public ContainerBasicRefinery(int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(id, inventory, new ItemStackHandler(10), new ContainerData() {
            @Override
            public int get(int p_39284_) {
                return 0;
            }

            @Override
            public void set(int p_39285_, int p_39286_) {

            }

            @Override
            public int getCount() {
                return 0;
            }
        }, buffer.readFluidStack(), buffer.readFluidStack(), buffer.readFluidStack(), buffer.readFluidStack());
    }

    public ContainerBasicRefinery(int id, Inventory inventory, ItemStackHandler Inventory, ContainerData field, FluidStack CrudeOilStack, FluidStack gasolinestack, FluidStack dieselstack, FluidStack fueloilstack) {
        super(BASIC_REFINERY_CONTAINER_TYPE, id);
        this.field = field;
        this.crudeoilstack = CrudeOilStack;
        this.gasolinestack = gasolinestack;
        this.dieselstack=dieselstack;
        this.fueloilstack = fueloilstack;

        this.addSlot(new CrudeOilBucketInSlot(Inventory, 0, 8, 7, registerFluids.CRUDE_OIL_SOURCE));
        this.addSlot(new BucketOutSlot(Inventory, 1, 8, 63));

        for(int i=0; i<3; i++){
            int xval = 67+(23*i);
            this.addSlot(new BucketInSlot(Inventory, 2+(i*2), xval, 7));
            this.addSlot(new BucketOutSlot(Inventory, 3+(i*2), xval, 63));
        }

        this.addSlot(new SlotItemHandler(Inventory, 8, 142, 35){
            @Override
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return false;
            }
        });

        this.addSlot(new FuelSlot(Inventory, 9, 37, 63));

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
    public boolean stillValid(Player playerIn) {
        return true;
    }

    public boolean isBurning() {
        return this.field.get(0)>0;
    }

    public float getBurnLeftScaled(){
        return (float)this.field.get(0)/this.field.get(1)* 12;
    }

    public boolean isActive(){
        return this.field.get(10)>0;
    }

    private static class FuelSlot extends SlotItemHandler{


        public FuelSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return ForgeHooks.getBurnTime(stack, SMELTING)>0;
        }
    }

    private static class CrudeOilBucketInSlot extends SlotItemHandler{

        private final Fluid fluid;

        public CrudeOilBucketInSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Fluid fluid) {
            super(itemHandler, index, xPosition, yPosition);
            this.fluid = fluid;
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return FluidUtil.getFluidContained(stack).isPresent() && FluidUtil.getFluidContained(stack).get().getFluid() == this.fluid;
        }
    }

    private static class BucketInSlot extends SlotItemHandler{

        public BucketInSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return FluidUtil.getFluidHandler(stack).isPresent();
        }
    }

    private static class BucketOutSlot extends SlotItemHandler{

        public BucketOutSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return false;
        }
    }

    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        ItemStack CopyofStackinSlot = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack StackinSlot = slot.getItem();
            CopyofStackinSlot = StackinSlot.copy();
            //Bucket outs
            if (index < 10) {
                if (!this.moveItemStackTo(StackinSlot, 10, 46, false)) {
                    return ItemStack.EMPTY;
                }

            } else {
                if (FluidUtil.getFluidContained(StackinSlot).isPresent() && FluidUtil.getFluidContained(StackinSlot).get().getFluid().is(ModTags.Fluids.CRUDEOIL)) {
                    if (!this.moveItemStackTo(StackinSlot, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (FluidUtil.getFluidContained(StackinSlot).isPresent() && FluidUtil.getFluidContained(StackinSlot).get().isEmpty()) {
                    if (!this.moveItemStackTo(StackinSlot, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                    else if (!this.moveItemStackTo(StackinSlot, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                    else if (!this.moveItemStackTo(StackinSlot, 6, 7, false)) {
                        return ItemStack.EMPTY;
                    }
                }else if (index < 37) {
                    if (!this.moveItemStackTo(StackinSlot, 37, 46, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 46 && !this.moveItemStackTo(StackinSlot, 10, 37, false)) {
                    return ItemStack.EMPTY;
                }else if (!this.moveItemStackTo(StackinSlot, 10, 37, false)) {
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

    public int getCrudeOilTankCapacity(){
        return this.field.get(3);
    }
    public int getCrudeOilTankAmount(){
        return this.field.get(2);
    }
    public int getGasolineTankCapacity(){
        return this.field.get(5);
    }
    public int getGasolineTankAmount(){
        return this.field.get(4);
    }
    public int getDieselTankCapacity(){
        return this.field.get(7);
    }
    public int getDieselTankAmount(){
        return this.field.get(6);
    }
    public int getFuelOilTankCapacity(){
        return this.field.get(9);
    }
    public int getFuelOilTankAmount(){
        return this.field.get(8);
    }
}

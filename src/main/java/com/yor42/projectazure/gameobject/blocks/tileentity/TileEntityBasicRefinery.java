package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.setup.register.registerFluids;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityBasicRefinery extends LockableTileEntity implements INamedContainerProvider, ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {

    /*
    Index:
    0: Crude Oil in
    1: Crude oil bucket out
    2: Gasoline Bucket in
    3: Gasoline Bucket out
    4: Diesel Bucket in
    5: Diesel Bucket out
    6: Fuel Oil Bucket in
    7: Fuel Oil Bucket out
    8: Bitumen out
     */


    public ItemStackHandler ITEMHANDLER = new ItemStackHandler(9){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

            if((slot % 2 == 0 && slot < 8)){
                if(stack.getItem() instanceof BucketItem){
                    Fluid fluid = ((BucketItem) stack.getItem()).getFluid();
                    if (slot == 0) {
                        return fluid == registerFluids.CRUDE_OIL_SOURCE;
                    }
                }
            }

            return false;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
        }
    };

    public FluidTank CrudeOilTank = new FluidTank(2000);

    protected TileEntityBasicRefinery(TileEntityType<?> p_i48285_1_) {
        super(p_i48285_1_);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {

        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, Direction direction) {
        return true;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("tileentity.basic_refinery");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }

    @Override
    public int getSizeInventory() {
        return this.ITEMHANDLER.getSlots();
    }

    @Override
    public boolean isEmpty() {
        return this.ITEMHANDLER.getStackInSlot(0).isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.ITEMHANDLER.getStackInSlot(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int i1) {
        this.ITEMHANDLER.getStackInSlot(i).shrink(i1);
        return this.ITEMHANDLER.getStackInSlot(i);
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {

    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public void clear() {
        this.ITEMHANDLER.setStackInSlot(0, ItemStack.EMPTY);
    }

    @Override
    public void fillStackedContents(RecipeItemHelper recipeItemHelper) {

    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> iRecipe) {

    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void tick() {

    }
}

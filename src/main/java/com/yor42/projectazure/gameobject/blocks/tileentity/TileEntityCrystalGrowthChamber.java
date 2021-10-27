package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.storages.DissolvedMaterial;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityCrystalGrowthChamber extends LockableTileEntity implements INamedContainerProvider, IRecipeHelperPopulator, ITickableTileEntity {

    public FluidTank waterTank = new FluidTank(4000, (fluidStack)->fluidStack.getFluid() == Fluids.WATER);
    public ItemStackHandler inventory = new ItemStackHandler(6);
    public DissolvedMaterial dissolvedMaterial = new DissolvedMaterial(1200);

    protected TileEntityCrystalGrowthChamber(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("block.projectazure.crystalgrowthchamber");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for(int i=0;i<this.inventory.getSlots();i++){
            if(!this.inventory.getStackInSlot(i).isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int i, int i1) {
        this.inventory.getStackInSlot(i).shrink(i1);
        return this.inventory.getStackInSlot(i);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if(index >= 0 && index < this.inventory.getSlots()){
            this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory.setStackInSlot(index, stack);
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        for(int i=0;i<this.inventory.getSlots(); i++){
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void fillStackedContents(RecipeItemHelper helper) {
        for(int i=0;i<this.inventory.getSlots();i++) {
            helper.accountStack(this.inventory.getStackInSlot(i));
        }
    }

    @Override
    public void tick() {

    }
}

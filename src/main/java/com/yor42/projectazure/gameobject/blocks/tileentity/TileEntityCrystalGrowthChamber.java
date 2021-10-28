package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.setup.register.registerFluids;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCrystalGrowthChamber extends LockableTileEntity implements INamedContainerProvider, IRecipeHelperPopulator, ITickableTileEntity {

    public FluidTank waterTank = new FluidTank(4000, (fluidStack)->fluidStack.getFluid() == Fluids.WATER);
    public FluidTank growthChamberLiquid = new FluidTank(4000);
    public ItemStackHandler inventory = new ItemStackHandler(6);
   private int growthProgress = 0;
    private int MaxGrowthProgress;
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
        if(!this.world.isRemote()){
            for(int i = 1; i<3; i++){
                if(this.tryFillingCrystalChamber(i)){
                    break;
                }
            }
        }
    }

    public boolean tryFillingCrystalChamber(int index2try){
        ItemStack stack = this.inventory.getStackInSlot(index2try);
        Item item = stack.getItem();
        FluidStack FluidToAdd = this.getAmountfromItem(item);
        if(this.growthChamberLiquid.fill(FluidToAdd, IFluidHandler.FluidAction.SIMULATE)>0){
            this.growthChamberLiquid.fill(FluidToAdd, IFluidHandler.FluidAction.EXECUTE);
            stack.shrink(1);
            return true;
        }
        return false;
    }

    public FluidStack getAmountfromItem(Item item){
        if(item == registerItems.ORIGINITE.get()){
            return new FluidStack(registerFluids.ORIGINIUM_SOLUTION_SOURCE, 500);
        }
        return FluidStack.EMPTY;
    }
}

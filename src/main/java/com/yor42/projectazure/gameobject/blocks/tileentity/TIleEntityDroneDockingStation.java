package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.storages.CustomEnergyStorage;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import java.util.ArrayList;

public class TIleEntityDroneDockingStation extends LockableTileEntity {

    protected boolean isDestination = false;
    protected final CustomEnergyStorage energyStorage;
    protected final ItemStackHandler inventory;

    protected boolean invertloaditem, invertunloaditem;
    protected final ArrayList<String> ItemtoLoad = new ArrayList<>();
    protected final ArrayList<String> ItemtounLoad = new ArrayList<>();


    protected TIleEntityDroneDockingStation(TileEntityType<?> typeIn) {
        super(typeIn);
        this.energyStorage = new CustomEnergyStorage(25000,2500,2000);
        this.inventory = new ItemStackHandler(18);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("tileentity.dronedockingstation.name");
    }

    @Override
    protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
        return null;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {

        compound.put("energy",this.energyStorage.serializeNBT());
        compound.put("inventory", this.inventory.serializeNBT());
        CompoundNBT compound2 = new CompoundNBT();
        int listsize = this.ItemtoLoad.size();
        for(int i=0; i<listsize;i++){
            compound2.putString("filter"+i, this.ItemtoLoad.get(i));
        }
        compound2.putInt("size", listsize);
        compound2.putBoolean("inverted", this.invertloaditem);
        compound.put("ItemtoLoad", compound2);

        compound2 = new CompoundNBT();
        listsize = this.ItemtounLoad.size();
        for(int i=0; i<listsize;i++){
            compound2.putString("filter"+i, this.ItemtounLoad.get(i));
        }
        compound2.putInt("size", listsize);
        compound2.putBoolean("inverted", this.invertunloaditem);
        compound.put("ItemtounLoad", compound2);

        return super.save(compound);
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT compound) {

        this.energyStorage.deserializeNBT(compound.getCompound("energy"));
        this.inventory.deserializeNBT(compound.getCompound("inventory"));

        CompoundNBT compound2 = compound.getCompound("ItemtoLoad");
        int filtersize = compound2.getInt("size");
        for(int i=0; i<filtersize; i++){
            this.ItemtoLoad.add(compound2.getString("filter"+i));
        }
        this.invertloaditem=compound2.getBoolean("inverted");

        compound2 = compound.getCompound("ItemtounLoad");
        filtersize = compound2.getInt("size");
        for(int i=0; i<filtersize; i++){
            this.ItemtounLoad.add(compound2.getString("filter"+i));
        }
        this.invertunloaditem=compound2.getBoolean("inverted");

        super.load(p_230337_1_, compound);
    }

    @Override
    public int getContainerSize() {
        return this.inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for(int i=0;i<this.inventory.getSlots(); i++){
            if(this.inventory.getStackInSlot(i) != ItemStack.EMPTY){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = this.inventory.getStackInSlot(index);
        stack.shrink(count);
        this.inventory.setStackInSlot(index, stack);
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (index >=0 && index < this.inventory.getSlots()){
            this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.inventory.setStackInSlot(index, stack);

        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {
        for(int i=0;i<this.inventory.getSlots(); i++){
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}

package com.yor42.projectazure.gameobject.storages;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;

public class CustomEnergyStorageItem implements IEnergyStorage {
    private final ItemStack stack;
    public CustomEnergyStorageItem(ItemStack stack, int capacity)
    {
        this(stack, capacity, capacity, capacity);
    }

    public CustomEnergyStorageItem(ItemStack stack, int capacity, int maxin, int maxout)
    {
        this.stack = stack;
        this.setCapacity(capacity);
        this.setMaxExtract(maxout);
        this.setMaxReceive(maxin);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(this.getMaxEnergyStored() - this.getEnergyStored(), Math.min(this.getMaxReceive(), maxReceive));
        if (!simulate) {
            int energy = this.getEnergyStored() + energyReceived;
            this.setEnergyStored(energy);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(getEnergyStored(), Math.min(this.getMaxExtract(), maxExtract));
        if (!simulate) {
            int energy = this.getEnergyStored() - energyExtracted;
            this.setEnergyStored(energy);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored()
    {
        return this.getCompound().getInt("energy");
    }

    @Override
    public int getMaxEnergyStored()
    {
        return this.getCompound().getInt("capacity");
    }

    public void setEnergyStored(int value){
        CompoundTag storage = this.getCompound();
        storage.putInt("energy", value);
        this.setCompound(storage);
    }

    public void setMaxExtract(int value){
        CompoundTag storage = this.getCompound();
        storage.putInt("maxExtract", value);
        this.setCompound(storage);
    }

    public void setMaxReceive(int value){
        CompoundTag storage = this.getCompound();
        storage.putInt("maxReceive", value);
        this.setCompound(storage);
    }

    public void setCapacity(int value){
        CompoundTag storage = this.getCompound();
        storage.putInt("capacity", value);
        this.setCompound(storage);
    }

    public int getMaxExtract()
    {
        return this.getCompound().getInt("maxExtract");
    }

    public int getMaxReceive()
    {
        return this.getCompound().getInt("maxReceive");
    }

    @Override
    public boolean canExtract()
    {
        return this.getCompound().getInt("maxExtract") > 0;
    }

    @Override
    public boolean canReceive()
    {
        return this.getCompound().getInt("maxReceive") > 0;
    }

    protected CompoundTag getCompound(){
        return this.stack.getOrCreateTag().getCompound("energystorage");
    }

    protected void setCompound(CompoundTag compoundNBT){
        this.stack.getOrCreateTag().put("energystorage", compoundNBT);
    }


}

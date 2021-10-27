package com.yor42.projectazure.gameobject.storages;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {
    public CustomEnergyStorage(int capacity) {
        super(capacity);
    }

    public CustomEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }
    /*
    Why This is not a default thing in a forge?
     */
    public CompoundNBT serializeNBT(){
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("energystorage_energy", this.energy);
        compound.putInt("energystorage_capacity", this.capacity);
        compound.putInt("energystorage_maxReceive", this.maxReceive);
        compound.putInt("energystorage_maxExtract", this.maxExtract);
        return compound;
    }

    public void deserializeNBT(CompoundNBT compound){
        this.energy = compound.getInt("energystorage_energy");
        this.capacity = compound.getInt("energystorage_capacity");
        this.maxReceive = compound.getInt("energystorage_maxReceive");
        this.maxExtract = compound.getInt("energystorage_maxExtract");
    }

    public void setEnergy(int value){
        this.energy = value;
    }

    public void setMaxEnergy(int value){
        this.capacity = value;
    }

}

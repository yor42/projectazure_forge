package com.yor42.projectazure.gameobject.storages;

import com.yor42.projectazure.gameobject.items.ItemSoluble;
import net.minecraft.command.impl.GiveCommand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

public class DissolvedMaterial {

    @Nullable
    protected Item DissolvedMaterial;
    protected int DissolvedAmount=0;
    protected int maxCapacity;

    public DissolvedMaterial(int capacity){
        this.maxCapacity = capacity;
    }

    public boolean DissolveItem(ItemSoluble item){
        if (canDissolve(item)) {
            this.DissolvedMaterial = item;
            this.DissolvedAmount += Math.min(item.SolubleAmount(), this.maxCapacity);
            return true;
        }
        return false;
    }

    public boolean canDissolve(ItemSoluble item){
        return this.DissolvedMaterial == item && this.DissolvedAmount+item.SolubleAmount()<=this.maxCapacity;
    }

    public boolean UseMaterial(Item item, int amount){
        if(item == this.DissolvedMaterial) {
            if (DissolvedAmount - amount > 0) {
                this.DissolvedAmount -= amount;
                return true;
            }
            this.DissolvedMaterial = null;
            return false;
        }
        return false;
    }

    public boolean UseMaterial(Item item){
        return this.UseMaterial(item, 1);
    }

    public CompoundNBT SerializeNBT(CompoundNBT tag2save){
        CompoundNBT compound = new CompoundNBT();
        if(this.DissolvedMaterial!=null){
            compound.putInt("material", Item.getIdFromItem(this.DissolvedMaterial));
        }
        compound.putInt("amount", this.DissolvedAmount);
        compound.putInt("capacity", this.maxCapacity);
        tag2save.put("dissolvedMaterial", compound);
        return tag2save;
    }

    public int getCapacity(){
        return this.maxCapacity;
    }

    public int getAmount(){
        return this.DissolvedAmount;
    }

    public void deserializeNBT(CompoundNBT tag){
        CompoundNBT compound = tag.getCompound("dissolvedMaterial");
        if(compound.contains("material")){
            this.DissolvedMaterial = Item.getItemById(tag.getInt("material"));
        }
        else{
            this.DissolvedMaterial = null;
        }
        this.DissolvedAmount = compound.getInt("amount");
        this.maxCapacity = compound.getInt("capacity");
    }

}

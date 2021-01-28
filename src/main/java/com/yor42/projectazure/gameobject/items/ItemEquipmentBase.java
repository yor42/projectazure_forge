package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.model.provider.GeoModelProvider;

public abstract class ItemEquipmentBase extends Item {


    protected enums.SLOTTYPE slot;
    //remaining ammo
    protected int ammo;
    //ammo capacity
    protected int maxAmmo;
    protected int reloadTime;
    protected int remainingReloadTime;

    protected int durability;

    public ItemEquipmentBase(Properties properties) {
        super(properties);
    }

    public void damageRigging(int amount){
        this.durability = this.durability-amount;
    }

    public boolean isBroken(){
        return this.durability == 0;
    }

    public boolean canfire(){
        return this.ammo != 0 && !this.isBroken();
    }

    public enums.SLOTTYPE getSlot() {
        return slot;
    }


}

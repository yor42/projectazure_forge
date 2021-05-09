package com.yor42.projectazure.gameobject.items.equipment;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public abstract class ItemEquipmentPlaneBase extends ItemEquipmentBase{

    public ItemEquipmentPlaneBase(Properties properties, int maxHP) {
        super(properties, maxHP);
        this.slot = enums.SLOTTYPE.PLANE;
    }

    @Override
    public void onUpdate(ItemStack stack) {
        CompoundNBT compound = stack.getOrCreateTag();
        int currentFuel = compound.getInt("fuel");
        if (currentFuel+FuelPerTick()<=this.getMaxOperativeTime()){
            currentFuel+=FuelPerTick();
        }
        else if(currentFuel+FuelPerTick()>this.getMaxOperativeTime()){
            currentFuel = this.getMaxOperativeTime();
        }

        compound.putInt("fuel", currentFuel);

        if(!compound.getBoolean("isArmed")){
            int Armdelay = compound.getInt("armDelay");
            if(Armdelay > 0){
                Armdelay--;
            }
            compound.putInt("armDelay", Armdelay);
        }

    }

    public abstract float getAttackDamage();

    public abstract EntityType<? extends AbstractEntityPlanes> getEntityType();

    public float getMovementSpeed(){
        return (float) this.getEntityType().create(ClientProxy.getClientWorld()).getAttribute(Attributes.MOVEMENT_SPEED).getValue();
    }

    public abstract int getreloadTime();

    public int FuelPerTick(){
        return 2;
    }

    public abstract enums.PLANE_TYPE getType();

    public abstract int getMaxOperativeTime();

}

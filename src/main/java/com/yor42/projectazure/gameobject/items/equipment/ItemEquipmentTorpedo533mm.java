package com.yor42.projectazure.gameobject.items.equipment;

import com.yor42.projectazure.client.model.modelEquipmentTorpedo533mm;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ItemEquipmentTorpedo533mm extends ItemEquipmentTorpedo implements IAnimatable {
    public ItemEquipmentTorpedo533mm(Properties properties, int maxHP) {
        super(properties, maxHP);
        this.isreloadable = false;
        this.firedelay = 27;
    }

    @Override
    public AnimatedGeoModel getEquipmentModel() {
        return new modelEquipmentTorpedo533mm();
    }

    @Override
    public void onUpdate(ItemStack stack) {
        CompoundNBT tags = stack.getOrCreateTag();
        int delay = tags.getInt("delay");
        if(delay >0)
            delay--;
        tags.putInt("delay", delay);
    }

    @Override
    public boolean canUseTorpedo(ItemStack stack) {
        CompoundNBT tags = stack.getOrCreateTag();
        int delay = tags.getInt("delay");
        int ammo = tags.getInt("ammo");
        return delay<=0;
        //return delay <= 0 && ammo>0;
    }

    @Override
    public void onFire(ItemStack equipmentStack) {
        CompoundNBT tags = equipmentStack.getOrCreateTag();
        ItemEquipmentBase equipmentItem = (ItemEquipmentBase) equipmentStack.getItem();
        tags.putInt("delay", equipmentItem.getFiredelay());
        //tags.putInt("ammo", tags.getInt("ammo")-1);
    }
}

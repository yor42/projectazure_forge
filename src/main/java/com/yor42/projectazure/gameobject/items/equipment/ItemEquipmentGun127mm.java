package com.yor42.projectazure.gameobject.items.equipment;

import com.yor42.projectazure.client.model.ModelMaingun127mm;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ItemEquipmentGun127mm extends ItemEquipmentGun{
    public ItemEquipmentGun127mm(Properties properties,int maxHP) {
        super(properties, maxHP);
        this.firedelay = 60;
    }

    @Override
    public AnimatedGeoModel getEquipmentModel() {
        return new ModelMaingun127mm();
    }

    @Override
    public void onUpdate(ItemStack stack) {
        CompoundNBT tags = stack.getOrCreateTag();
        int delay = tags.getInt("delay");
        if(delay>0)
            delay--;
        tags.putInt("delay", delay);
    }
}

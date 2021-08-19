package com.yor42.projectazure.gameobject.items.equipment;

import com.yor42.projectazure.client.model.equipments.modelEquipmentTorpedo533mm;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ItemEquipmentTorpedo533Mm extends ItemEquipmentTorpedo implements IAnimatable {
    public ItemEquipmentTorpedo533Mm(Properties properties, int maxHP) {
        super(properties, maxHP);
        this.isreloadable = false;
        this.MaxAmmoCap = 4;
        this.firedelay = 639;
    }

    @Override
    public AnimatedGeoModel getEquipmentModel() {
        return new modelEquipmentTorpedo533mm();
    }

    @Override
    public void onUpdate(ItemStack EquipmentStack, ItemStack RiggingStack) {
        CompoundNBT tags = EquipmentStack.getOrCreateTag();
        int delay = tags.getInt("delay");
        if(delay >0)
            delay--;
        tags.putInt("delay", delay);
    }
}

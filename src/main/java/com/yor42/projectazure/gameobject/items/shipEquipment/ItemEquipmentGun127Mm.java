package com.yor42.projectazure.gameobject.items.shipEquipment;

import com.yor42.projectazure.client.model.equipments.ModelMaingun127mm;
import com.yor42.projectazure.libs.enums;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ItemEquipmentGun127Mm extends ItemEquipmentGun{
    public ItemEquipmentGun127Mm(Properties properties, int maxHP) {
        super(properties, maxHP);
        this.slot = enums.SLOTTYPE.SUB_GUN;
        this.firedelay = 60;
    }

    @Override
    public AnimatedGeoModel getEquipmentModel() {
        return new ModelMaingun127mm();
    }

    @Override
    public void onUpdate(ItemStack EquipmentStack, ItemStack RiggingStack) {
        CompoundTag tags = EquipmentStack.getOrCreateTag();
        int delay = tags.getInt("delay");
        if(delay>0)
            delay--;
        tags.putInt("delay", delay);
    }
}

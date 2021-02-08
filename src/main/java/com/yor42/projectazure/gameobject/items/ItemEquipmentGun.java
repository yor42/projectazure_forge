package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public abstract class ItemEquipmentGun extends ItemEquipmentBase{

    public ItemEquipmentGun(Properties properties) {
        super(properties);
        this.slot = enums.SLOTTYPE.GUN;
    }

    @Override
    public boolean canUseTorpedo(ItemStack stack) {
        return false;
    }
}

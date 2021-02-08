package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.item.ItemStack;

public abstract class ItemEquipmentTorpedo extends ItemEquipmentBase {

    protected boolean isreloadable;

    public ItemEquipmentTorpedo(Properties properties) {
        super(properties);
        this.slot = enums.SLOTTYPE.TORPEDO;
    }

    @Override
    public boolean canUseCanon(ItemStack stack) {
        return false;
    }
}

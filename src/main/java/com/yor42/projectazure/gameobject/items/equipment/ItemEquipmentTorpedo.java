package com.yor42.projectazure.gameobject.items.equipment;

import com.yor42.projectazure.libs.enums;
import net.minecraft.item.ItemStack;

public abstract class ItemEquipmentTorpedo extends ItemEquipmentBase {

    protected boolean isreloadable;

    public ItemEquipmentTorpedo(Properties properties,int maxHP) {
        super(properties, maxHP);
        this.slot = enums.SLOTTYPE.TORPEDO;
    }
}

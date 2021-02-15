package com.yor42.projectazure.gameobject.items.equipment;

import com.yor42.projectazure.libs.enums;
import net.minecraft.item.ItemStack;

public abstract class ItemEquipmentGun extends ItemEquipmentBase{

    protected enums.CanonSize size;

    public ItemEquipmentGun(Properties properties, int maxHP) {
        super(properties, maxHP);
        this.slot = enums.SLOTTYPE.GUN;
    }

    public enums.CanonSize getSize(){
        return this.size;
    }
}

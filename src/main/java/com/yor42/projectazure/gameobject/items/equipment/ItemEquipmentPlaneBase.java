package com.yor42.projectazure.gameobject.items.equipment;

import com.yor42.projectazure.libs.enums;

public abstract class ItemEquipmentPlaneBase extends ItemEquipmentBase{

    private int maxFuel;
    private enums.PLANE_TYPE type;
    public ItemEquipmentPlaneBase(Properties properties, int maxHP) {
        super(properties, maxHP);
        this.slot = enums.SLOTTYPE.PLANE;
    }

    public void setType(enums.PLANE_TYPE type) {
        this.type = type;
    }

    public enums.PLANE_TYPE getType() {
        return this.type;
    }


}

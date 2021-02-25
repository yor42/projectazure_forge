package com.yor42.projectazure.gameobject.items.equipment;

import com.yor42.projectazure.libs.enums;

public abstract class ItemEquipmentPlane extends ItemEquipmentBase{

    private enums.PLANE_TYPE type;

    public ItemEquipmentPlane(Properties properties, int maxHP) {
        super(properties, maxHP);
    }

    public void setType(enums.PLANE_TYPE type) {
        this.type = type;
    }

    public enums.PLANE_TYPE getType() {
        return this.type;
    }
}

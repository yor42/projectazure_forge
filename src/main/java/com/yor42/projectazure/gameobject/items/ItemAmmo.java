package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;

public class ItemAmmo extends itemBaseTooltip{
    enums.AmmoTypes ammoType;
    public ItemAmmo(Properties properties, enums.AmmoTypes AmmoType) {
        super(properties);
        this.ammoType = AmmoType;
    }

    public enums.AmmoTypes getAmmoType() {
        return this.ammoType;
    }
}

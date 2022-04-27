package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.world.item.Item;

public class ItemAmmo extends Item {

    private final enums.AmmoCalibur calibur;
    private final int ammoCount;

    public ItemAmmo(enums.AmmoCalibur calibur, int ammos, Properties properties) {
        super(properties);
        this.calibur = calibur;
        this.ammoCount = ammos;
    }

    public int getAmmoCount() {
        return this.ammoCount;
    }

    public enums.AmmoCalibur getCalibur() {
        return this.calibur;
    }
}

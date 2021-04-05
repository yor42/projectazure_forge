package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import net.minecraft.item.ItemStack;

public class ItemMagazine extends ItemBaseTooltip{

    private enums.AmmoCalibur calibur;
    private int Ammocount;

    public ItemMagazine(enums.AmmoCalibur calibur, int AmmoCount, Properties properties) {
        super(properties);
        this.calibur = calibur;
        this.Ammocount = AmmoCount;
    }

    public int getMagCap() {
        return this.Ammocount;
    }

    public enums.AmmoCalibur getCalibur() {
        return this.calibur;
    }
}

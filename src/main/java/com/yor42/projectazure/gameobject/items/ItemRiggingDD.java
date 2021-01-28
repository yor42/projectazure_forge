package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.enums;
import software.bernie.geckolib3.core.IAnimatable;

public abstract class ItemRiggingDD extends ItemRiggingBase implements IAnimatable {

    public ItemRiggingDD(Properties properties) {
        super(properties);
        this.validclass = enums.shipClass.Destroyer;
    }
}

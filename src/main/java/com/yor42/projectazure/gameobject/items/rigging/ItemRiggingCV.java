package com.yor42.projectazure.gameobject.items.rigging;

import com.yor42.projectazure.libs.enums;
import software.bernie.geckolib3.core.IAnimatable;

public abstract class ItemRiggingCV extends ItemRiggingBase implements IAnimatable {
    public ItemRiggingCV(Properties properties, int HP) {
        super(properties, HP);
        this.validclass = enums.shipClass.AircraftCarrier;
    }
}

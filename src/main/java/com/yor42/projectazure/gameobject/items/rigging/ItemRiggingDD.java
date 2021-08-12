package com.yor42.projectazure.gameobject.items.rigging;

import com.yor42.projectazure.libs.enums;
import software.bernie.geckolib3.core.IAnimatable;

public abstract class ItemRiggingDD extends ItemRiggingBase implements IAnimatable {

    public ItemRiggingDD(Properties properties, int HP) {
        super(properties, HP);
        this.validclass = enums.shipClass.Destroyer;
        /*
        this.EquipmentRotation = new Quaternion[]{
                new Quaternion(0, 0, -90, true),
                new Quaternion(0, 0, 90, true),
                new Quaternion(0, 0, -90, true),
                new Quaternion(90, 180, 0, true),
                new Quaternion(0, 0, 90, true)
        };

         */
    }
}

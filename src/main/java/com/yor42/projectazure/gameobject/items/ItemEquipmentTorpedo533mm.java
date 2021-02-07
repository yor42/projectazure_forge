package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.client.model.modelEquipmentTorpedo533mm;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ItemEquipmentTorpedo533mm extends ItemEquipmentTorpedo implements IAnimatable {
    public ItemEquipmentTorpedo533mm(Properties properties) {
        super(properties);
        this.isreloadable = false;
    }

    @Override
    public AnimatedGeoModel getEquipmentModel() {
        return new modelEquipmentTorpedo533mm();
    }
}

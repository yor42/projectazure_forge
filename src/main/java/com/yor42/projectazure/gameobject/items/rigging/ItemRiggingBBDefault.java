package com.yor42.projectazure.gameobject.items.rigging;

import com.yor42.projectazure.client.model.rigging.modelBBRiggingDefault;
import com.yor42.projectazure.libs.enums;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ItemRiggingBBDefault extends ItemRiggingBase{
    public ItemRiggingBBDefault(Properties properties, int HP) {
        super(properties, HP);
        this.validclass = enums.shipClass.Battleship;
    }

    @Override
    public int getGunSlotCount() {
        return 5;
    }

    @Override
    public int getAASlotCount() {
        return 3;
    }

    @Override
    public int getTorpedoSlotCount() {
        return 1;
    }

    @Override
    public AnimatedGeoModel getModel() {
        return new modelBBRiggingDefault();
    }
}

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
    public int getMainGunSlotCount() {
        return 3;
    }

    @Override
    public int getSubGunSlotCount() {
        return 2;
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
    public int getFuelTankCapacity() {
        return 5000;
    }

    @Override
    public AnimatedGeoModel getModel() {
        return new modelBBRiggingDefault();
    }
}

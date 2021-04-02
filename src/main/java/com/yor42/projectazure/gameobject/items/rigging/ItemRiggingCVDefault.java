package com.yor42.projectazure.gameobject.items.rigging;

import com.yor42.projectazure.client.model.rigging.modelCVRiggingDefault;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ItemRiggingCVDefault extends ItemRiggingCV{

    public ItemRiggingCVDefault(Properties properties, int HP) {
        super(properties, HP);
    }

    @Override
    public int getMainGunSlotCount() {
        return 0;
    }

    @Override
    public int getSubGunSlotCount() {
        return 2;
    }

    @Override
    public int getAASlotCount() {
        return 4;
    }

    @Override
    public int getTorpedoSlotCount() {
        return 0;
    }

    @Override
    public int getHangerSlots() {
        return 45;
    }

    @Override
    public AnimatedGeoModel getModel() {
        return new modelCVRiggingDefault();
    }
}

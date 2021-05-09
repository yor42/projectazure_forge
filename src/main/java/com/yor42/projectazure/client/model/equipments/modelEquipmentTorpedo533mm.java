package com.yor42.projectazure.client.model.equipments;

import com.yor42.projectazure.libs.defined;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class modelEquipmentTorpedo533mm extends AnimatedGeoModel {

    //equipment_torpedo_533mm.geo.json

    @Override
    public ResourceLocation getModelLocation(Object o) {
        return new ResourceLocation(defined.MODID,"geo/equipment/equipment_torpedo_533mm.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object o) {
        return new ResourceLocation(defined.MODID,"textures/equipments/equipment_533mm_torpedo.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object o) {
        return null;
    }
}

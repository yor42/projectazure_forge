package com.yor42.projectazure.client.model.rigging;

import com.yor42.projectazure.gameobject.items.ItemRiggingDD;
import com.yor42.projectazure.libs.defined;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class modelDDRiggingDefault extends AnimatedGeoModel {

    @Override
    public ResourceLocation getModelLocation(Object o) {
        return new ResourceLocation(defined.MODID,"geo/rigging/modelrigging_dd_default.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object o) {
        return new ResourceLocation(defined.MODID,"textures/item/rigging_dd_default.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object o) {
        return new ResourceLocation(defined.MODID,"animations/rigging/rigging_dd_default.animation.json");
    }
}

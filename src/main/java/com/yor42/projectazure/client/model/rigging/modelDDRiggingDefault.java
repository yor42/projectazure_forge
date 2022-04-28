package com.yor42.projectazure.client.model.rigging;

import com.yor42.projectazure.libs.Constants;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class modelDDRiggingDefault extends AnimatedGeoModel {

    @Override
    public ResourceLocation getModelLocation(Object o) {
        return new ResourceLocation(Constants.MODID,"geo/rigging/modelrigging_dd_default.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object o) {
        return new ResourceLocation(Constants.MODID,"textures/rigging/rigging_dd_default.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object o) {
        return new ResourceLocation(Constants.MODID,"animations/rigging/rigging_dd_default.animation.json");
    }
}

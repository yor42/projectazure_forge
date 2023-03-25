package com.yor42.projectazure.client.model.rigging;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class modelBBRiggingDefault extends AnimatedGeoModel {

    @Override
    public ResourceLocation getModelLocation(Object o) {
        return ModResourceLocation("geo/rigging/modelrigging_bb_default.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object o) {
        return ModResourceLocation("textures/rigging/rigging_bb_default.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object o) {
        return ModResourceLocation("animations/rigging/rigging_dd_default.animation.json");
    }

}

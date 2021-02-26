package com.yor42.projectazure.client.model.planes;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class modelPlaneF4FWildCat extends AnimatedGeoModel {
    @Override
    public void setLivingAnimations(Object o, Integer integer, AnimationEvent animationEvent) {

    }

    @Override
    public ResourceLocation getModelLocation(Object o) {
        return ModResourceLocation("geo/misc/airplanes/f4f_wildcat.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object o) {
        return ModResourceLocation("textures/planes/f4f_wildcat.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object o) {
        return ModResourceLocation("animations/entity/misc/f4fwildcat.animation.json");
    }
}

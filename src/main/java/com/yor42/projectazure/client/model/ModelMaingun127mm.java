package com.yor42.projectazure.client.model;

import com.yor42.projectazure.libs.defined;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelMaingun127mm extends AnimatedGeoModel {
    @Override
    public void setLivingAnimations(Object o, Integer integer, AnimationEvent animationEvent) {

    }

    @Override
    public ResourceLocation getModelLocation(Object o) {
        return new ResourceLocation(defined.MODID,"geo/equipment/equipment_maingun_127mm.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object o) {
        return new ResourceLocation(defined.MODID,"textures/equipments/equipment_127mm_maingun.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object o) {
        return null;
    }
}

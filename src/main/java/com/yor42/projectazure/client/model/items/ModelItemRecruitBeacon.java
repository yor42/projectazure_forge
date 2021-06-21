package com.yor42.projectazure.client.model.items;

import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.List;

public class ModelItemRecruitBeacon extends AnimatedGeoModel {

    @Override
    public ResourceLocation getModelLocation(Object o) {
        return ResourceUtils.ModelLocation("block/recruit_beacon.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object o) {
        return ResourceUtils.ModResourceLocation("textures/block/recruit_beacon.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object o) {
        return null;
    }
}

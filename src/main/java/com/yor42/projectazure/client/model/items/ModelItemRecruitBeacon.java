package com.yor42.projectazure.client.model.items;

import com.yor42.projectazure.gameobject.items.AnimateableMachineBlockItems;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelItemRecruitBeacon extends AnimatedGeoModel<AnimateableMachineBlockItems> {

    @Override
    public ResourceLocation getModelLocation(AnimateableMachineBlockItems o) {
        return ResourceUtils.ModelLocation("item/recruit_beacon_item.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(AnimateableMachineBlockItems o) {
        return ResourceUtils.ModResourceLocation("textures/block/recruit_beacon.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(AnimateableMachineBlockItems o) {
        return null;
    }
}

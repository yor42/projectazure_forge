package com.yor42.projectazure.client.model.items;

import com.yor42.projectazure.gameobject.items.shipEquipment.ItemPlanef4Fwildcat;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class modelItemPlaneF4FWildCat extends AnimatedGeoModel<ItemPlanef4Fwildcat> {
    @Override
    public void setLivingAnimations(ItemPlanef4Fwildcat o, Integer integer, AnimationEvent animationEvent) {

    }

    @Override
    public ResourceLocation getModelLocation(ItemPlanef4Fwildcat o) {
        return ModResourceLocation("geo/misc/airplanes/f4f_wildcat.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemPlanef4Fwildcat o) {
        return ModResourceLocation("textures/planes/f4f_wildcat.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemPlanef4Fwildcat o) {
        return ModResourceLocation("animations/entity/misc/f4fwildcat.animation.json");
    }

    @Override
    public void setLivingAnimations(ItemPlanef4Fwildcat entity, Integer uniqueID) {

    }
}

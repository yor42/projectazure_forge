package com.yor42.projectazure.client.model.items;

import com.yor42.projectazure.gameobject.entity.misc.EntityF4fWildcat;
import com.yor42.projectazure.gameobject.items.equipment.ItemPlanef4fwildcat;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class modelItemPlaneF4FWildCat extends AnimatedGeoModel<ItemPlanef4fwildcat> {
    @Override
    public void setLivingAnimations(ItemPlanef4fwildcat o, Integer integer, AnimationEvent animationEvent) {

    }

    @Override
    public ResourceLocation getModelLocation(ItemPlanef4fwildcat o) {
        return ModResourceLocation("geo/misc/airplanes/f4f_wildcat.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemPlanef4fwildcat o) {
        return ModResourceLocation("textures/planes/f4f_wildcat.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemPlanef4fwildcat o) {
        return ModResourceLocation("animations/entity/misc/f4fwildcat.animation.json");
    }

    @Override
    public void setLivingAnimations(ItemPlanef4fwildcat entity, Integer uniqueID) {

    }
}

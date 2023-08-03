package com.yor42.projectazure.client.model.armor;

import com.yor42.projectazure.gameobject.items.ItemNightVisionHelmet;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GeoNightvisionModel extends AnimatedGeoModel<ItemNightVisionHelmet> {
    @Override
    public ResourceLocation getModelLocation(ItemNightVisionHelmet object) {
        return ResourceUtils.ModelLocation("armor/nightvision.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemNightVisionHelmet object) {
        return ResourceUtils.TextureLocation("armor/nvg");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemNightVisionHelmet animatable) {
        return ResourceUtils.AnimationLocation("block/armor/nightvision.animation.json");
    }
}

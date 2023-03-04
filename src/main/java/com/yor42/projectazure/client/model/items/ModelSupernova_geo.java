package com.yor42.projectazure.client.model.items;

import com.yor42.projectazure.gameobject.items.GeoGunItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class ModelSupernova_geo extends AnimatedGeoModel<GeoGunItem> {
    @Override
    public ResourceLocation getModelLocation(GeoGunItem object) {
        return ModResourceLocation("geo/item/supernova.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GeoGunItem object) {
        return ModResourceLocation("textures/item/supernova.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GeoGunItem animatable) {
        return ModResourceLocation("animations/item/gun/supernova.animation.json");
    }
}

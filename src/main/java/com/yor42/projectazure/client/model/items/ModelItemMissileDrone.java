package com.yor42.projectazure.client.model.items;

import com.yor42.projectazure.gameobject.items.ItemMissleDrone;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class ModelItemMissileDrone extends AnimatedGeoModel<ItemMissleDrone> {
    @Override
    public ResourceLocation getModelLocation(ItemMissleDrone object) {
        return ModResourceLocation("geo/misc/airplanes/modelmissiledrone.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemMissleDrone object) {
        return ModResourceLocation("textures/planes/missiledrone.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemMissleDrone animatable) {
        return ModResourceLocation("animations/entity/misc/missiledrone.animation.json");
    }
}

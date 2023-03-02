package com.yor42.projectazure.client.model.items;

import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.yor42.projectazure.gameobject.items.GeoGunItem;
import com.yor42.projectazure.gameobject.items.tools.ItemDefibPaddle;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class ModelTyphoon_geo extends AnimatedGeoModel<GeoGunItem> {
    @Override
    public ResourceLocation getModelLocation(GeoGunItem object) {
        return ModResourceLocation("geo/item/typhoon.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GeoGunItem object) {
        return ModResourceLocation("textures/item/typhoon.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GeoGunItem animatable) {
        return ModResourceLocation("animations/item/defib_paddle.animation.json");
    }
}

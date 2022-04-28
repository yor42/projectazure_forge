package com.yor42.projectazure.client.model.items;

import com.yor42.projectazure.gameobject.items.tools.ItemDefibPaddle;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class ModelDefibPaddle extends AnimatedGeoModel<ItemDefibPaddle> {
    @Override
    public ResourceLocation getModelLocation(ItemDefibPaddle object) {
        return ModResourceLocation("geo/item/defib_paddle.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemDefibPaddle object) {
        return ModResourceLocation("textures/item/defib.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemDefibPaddle animatable) {
        return ModResourceLocation("animations/item/defib_paddle.animation.json");
    }
}

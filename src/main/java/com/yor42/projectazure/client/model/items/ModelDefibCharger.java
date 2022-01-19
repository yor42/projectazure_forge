package com.yor42.projectazure.client.model.items;

import com.yor42.projectazure.gameobject.items.tools.ItemDefibCharger;
import com.yor42.projectazure.gameobject.items.tools.ItemDefibPaddle;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class ModelDefibCharger extends AnimatedGeoModel<ItemDefibCharger> {
    @Override
    public ResourceLocation getModelLocation(ItemDefibCharger object) {
        return ModResourceLocation("geo/item/defib_charger.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemDefibCharger object) {
        return ModResourceLocation("textures/item/defib_charger.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemDefibCharger animatable) {
        return ModResourceLocation("animations/item/defib.animation.json");
    }
}

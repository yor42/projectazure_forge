package com.yor42.projectazure.client.model.items;

import com.yor42.projectazure.gameobject.items.gun.ItemAbydos550;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class modelItemAbydos550 extends AnimatedGeoModel<ItemAbydos550> {

    @Override
    public ResourceLocation getModelLocation(ItemAbydos550 itemAbydos550) {
        return ModResourceLocation("geo/item/abydos550.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemAbydos550 itemAbydos550) {
        return ModResourceLocation("textures/item/itemabydos550.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemAbydos550 itemAbydos550) {
        return ModResourceLocation("animations/item/gun/abydos550.animation.json");
    }
}

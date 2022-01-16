package com.yor42.projectazure.client.model.items;

import com.yor42.projectazure.gameobject.items.tools.ItemClaymore;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.*;

public class ModelItemClaymore extends AnimatedGeoModel<ItemClaymore> {
    @Override
    public ResourceLocation getModelLocation(ItemClaymore object) {
        return ModelLocation("misc/modelclaymore1");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemClaymore object) {
        return TextureLocation("entity/modelclaymore1");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemClaymore animatable) {
        return ModResourceLocation("animations/entity/misc/modelclaymore1.animation.json");
    }
}

package com.yor42.projectazure.client.model.entity.misc;

import com.yor42.projectazure.gameobject.entity.misc.EntityClaymore;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.*;

public class ModelClaymore extends AnimatedGeoModel<EntityClaymore> {

    @Override
    public ResourceLocation getModelLocation(EntityClaymore object) {
        return ModelLocation("misc/modelclaymore");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityClaymore object) {
        return TextureLocation("entity/modelclaymore");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityClaymore animatable) {
        return ModResourceLocation("animations/entity/misc/modelclaymore.animation.json");
    }
}

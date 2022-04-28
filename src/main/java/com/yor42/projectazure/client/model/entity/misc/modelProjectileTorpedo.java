package com.yor42.projectazure.client.model.entity.misc;

import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileTorpedo;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.*;

public class modelProjectileTorpedo extends AnimatedGeoModel<EntityProjectileTorpedo> {
    @Override
    public ResourceLocation getModelLocation(EntityProjectileTorpedo entityProjectileTorpedo) {
        return ModelLocation("misc/modeltorpedoprojectile");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityProjectileTorpedo entityProjectileTorpedo) {
        return TextureLocation("entity/projectile/torpedo_projectile");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityProjectileTorpedo entityProjectileTorpedo) {
        return ModResourceLocation("animations/entity/misc/projectiletorpedo.animation.json");
    }
}

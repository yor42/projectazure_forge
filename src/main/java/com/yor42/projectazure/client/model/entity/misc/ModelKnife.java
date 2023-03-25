package com.yor42.projectazure.client.model.entity.misc;

import com.yor42.projectazure.gameobject.entity.projectiles.EntityThrownKnifeProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.ResourceUtils.*;

public class ModelKnife extends AnimatedGeoModel<EntityThrownKnifeProjectile> {
    @Override
    public ResourceLocation getModelLocation(EntityThrownKnifeProjectile object) {
        return ModelLocation("misc/modeltacticalknife");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityThrownKnifeProjectile object) {
        return TextureLocation("item/tactical_knife");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityThrownKnifeProjectile animatable) {
        return ModResourceLocation("animations/entity/misc/tactical_knife.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityThrownKnifeProjectile entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
    }
}

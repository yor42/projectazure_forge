package com.yor42.projectazure.client.model.planes;

import com.yor42.projectazure.gameobject.entity.planes.EntityF4fWildcat;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class modelEntityPlaneF4FWildCat extends AnimatedGeoModel<EntityF4fWildcat> {

    @Override
    public ResourceLocation getModelLocation(EntityF4fWildcat o) {
        return ModResourceLocation("geo/misc/airplanes/f4f_wildcat.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityF4fWildcat o) {
        return ModResourceLocation("textures/planes/f4f_wildcat.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityF4fWildcat o) {
        return ModResourceLocation("animations/entity/misc/f4fwildcat.animation.json");
    }
    @Override
    public void setCustomAnimations(EntityF4fWildcat animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        IBone bone = this.getAnimationProcessor().getBone("Plane");

        Vec3 movement = animatable.getDeltaMovement();
        bone.setRotationY(MathUtil.DegreeToRadian((float) ((Math.PI/2)-((float) Math.acos(movement.z()/ movement.length())))));
        bone.setRotationX(MathUtil.DegreeToRadian((float) Math.atan(movement.y()/ movement.x())));
    }
}

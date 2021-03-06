package com.yor42.projectazure.client.model.planes;

import com.yor42.projectazure.gameobject.entity.misc.EntityF4fWildcat;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

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
    public void setLivingAnimations(EntityF4fWildcat entity, Integer uniqueID) {
        IBone bone = this.getAnimationProcessor().getBone("Plane");

        Vector3d movement = entity.getMotion();
        bone.setRotationY(MathUtil.DegreeToRadian((float) ((Math.PI/2)-((float) Math.acos(movement.getZ()/ movement.length())))));
        bone.setRotationX(MathUtil.DegreeToRadian((float) Math.atan(movement.getY()/ movement.getX())));
        super.setLivingAnimations(entity, uniqueID);
    }
}

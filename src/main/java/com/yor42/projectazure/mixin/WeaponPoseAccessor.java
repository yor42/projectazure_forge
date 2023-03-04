package com.yor42.projectazure.mixin;

import com.tac.guns.client.render.pose.AimPose;
import com.tac.guns.client.render.pose.WeaponPose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WeaponPose.class)
public interface WeaponPoseAccessor {

    @Invoker(value = "getUpPose", remap = false)
    AimPose ongetUpPose();

    @Invoker(value = "getForwardPose", remap = false)
    AimPose ongetForwardPose();

   @Invoker(value = "getDownPose", remap = false)
    AimPose ongetDownPose();

    @Invoker(value = "hasAimPose", remap = false)
    boolean onHasAimPose();
}

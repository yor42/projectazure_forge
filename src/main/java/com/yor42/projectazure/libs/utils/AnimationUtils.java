package com.yor42.projectazure.libs.utils;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CrossbowItem;
import software.bernie.geckolib3.core.processor.IBone;

public class AnimationUtils {

    public static void GeckolibanimateCrossbowHold(IBone p_239104_0_, IBone p_239104_1_, IBone p_239104_2_, boolean p_239104_3_) {
        IBone modelrenderer = p_239104_3_ ? p_239104_0_ : p_239104_1_;
        IBone modelrenderer1 = p_239104_3_ ? p_239104_1_ : p_239104_0_;
        modelrenderer.setRotationY((p_239104_3_ ? 0.3F : -0.3F) + p_239104_2_.getRotationY());
        modelrenderer1.setRotationY((p_239104_3_ ? -0.6F : 0.6F) + p_239104_2_.getRotationY());
        modelrenderer.setRotationX(((float)Math.PI / 2F) + p_239104_2_.getRotationX() + 0.1F);
        modelrenderer1.setRotationX(1.5F + p_239104_2_.getRotationX());
    }

    public static void GeckolibanimateCrossbowCharge(IBone p_239102_0_, IBone p_239102_1_, LivingEntity p_239102_2_, boolean isMainHand) {
        IBone bone = isMainHand ? p_239102_0_ : p_239102_1_;
        IBone bone1 = isMainHand ? p_239102_1_ : p_239102_0_;
        bone.setRotationY(isMainHand ? 0.8F : -0.8F);
        bone.setRotationX(0.97079635F);
        bone1.setRotationX(bone.getRotationX());

        float f = (float) CrossbowItem.getChargeDuration(p_239102_2_.getUseItem());
        float f1 = Mth.clamp((float)p_239102_2_.getTicksUsingItem(), 0.0F, f);
        float f2 = f1 / f;

        bone1.setRotationY(Mth.lerp(f2, 0.4F, 0.85F) * (float)(isMainHand ? -1 : 1));
        bone1.setRotationX(Mth.lerp(f2, bone1.getRotationX(), ((float)Math.PI / 2F)));
    }

}

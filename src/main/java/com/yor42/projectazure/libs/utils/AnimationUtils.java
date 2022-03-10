package com.yor42.projectazure.libs.utils;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.processor.IBone;

public class AnimationUtils {

    public static void GeckolibanimateCrossbowHold(IBone p_239104_0_, IBone p_239104_1_, IBone p_239104_2_, boolean p_239104_3_) {
        IBone modelrenderer = p_239104_3_ ? p_239104_0_ : p_239104_1_;
        IBone modelrenderer1 = p_239104_3_ ? p_239104_1_ : p_239104_0_;
        modelrenderer.setRotationY((p_239104_3_ ? -0.3F : 0.3F) + p_239104_2_.getRotationY());
        modelrenderer1.setRotationY((p_239104_3_ ? 0.6F : -0.6F) + p_239104_2_.getRotationY());
        modelrenderer.setRotationX((-(float)Math.PI / 2F) + p_239104_2_.getRotationX() + 0.1F);
        modelrenderer1.setRotationX(-1.5F + p_239104_2_.getRotationX());
    }

    public static void GeckolibanimateCrossbowCharge(IBone p_239102_0_, IBone p_239102_1_, LivingEntity p_239102_2_, boolean isMainHand) {
        IBone bone = isMainHand ? p_239102_0_ : p_239102_1_;
        IBone bone1 = isMainHand ? p_239102_1_ : p_239102_0_;
        bone.setRotationY(isMainHand ? -0.8F : 0.8F);
        bone.setRotationX(-0.97079635F);
        bone1.setRotationX(bone.getRotationX());

        float f = (float) CrossbowItem.getChargeDuration(p_239102_2_.getUseItem());
        float f1 = MathHelper.clamp((float)p_239102_2_.getTicksUsingItem(), 0.0F, f);
        float f2 = f1 / f;

        bone1.setRotationY(MathHelper.lerp(f2, 0.4F, 0.85F) * (float)(isMainHand ? 1 : -1));
        bone1.setRotationX(MathHelper.lerp(f2, bone1.getRotationX(), (-(float)Math.PI / 2F)));
    }

    public static <T extends MobEntity> void GeckolibswingWeaponDown(IBone bone1, IBone bone2, T mob, float p_239103_3_, float p_239103_4_) {
        float f = MathHelper.sin(p_239103_3_ * (float)Math.PI);
        float f1 = MathHelper.sin((1.0F - (1.0F - p_239103_3_) * (1.0F - p_239103_3_)) * (float)Math.PI);
        bone1.setRotationZ(0.0F);
        bone2.setRotationZ(0.0F);
        bone1.setRotationY(0.15707964F);
        bone2.setRotationY(-0.15707964F);
        if (mob.getMainArm() == HandSide.RIGHT) {
            bone1.setRotationX(-1.8849558F + MathHelper.cos(p_239103_4_ * 0.09F) * 0.15F);
            bone2.setRotationX(-0.0F + MathHelper.cos(p_239103_4_ * 0.19F) * 0.5F);
            bone1.setRotationX(bone1.getRotationX() + f * 2.2F - f1 * 0.4F);
            bone2.setRotationX(bone2.getRotationX() + f * 1.2F - f1 * 0.4F);
        } else {
            bone1.setRotationX(-0.0F + MathHelper.cos(p_239103_4_ * 0.19F) * 0.5F);
            bone2.setRotationX(-1.8849558F + MathHelper.cos(p_239103_4_ * 0.09F) * 0.15F);
            bone1.setRotationX(bone1.getRotationX()+f * 1.2F - f1 * 0.4F);
            bone2.setRotationX(bone1.getRotationX()+f * 2.2F - f1 * 0.4F);
        }

        GeckolibbobArms(bone1, bone2, p_239103_4_);
    }

    public static void GeckolibbobArms(IBone bone1, IBone bone2, float value) {
        bone1.setRotationZ(bone1.getRotationZ() + MathHelper.cos(value * 0.09F) * 0.05F + 0.05F);
        bone2.setRotationZ(bone2.getRotationZ() - MathHelper.cos(value * 0.09F) * 0.05F + 0.05F);
        bone1.setRotationX(bone1.getRotationX() + MathHelper.cos(value * 0.09F) * 0.05F + 0.05F);
        bone2.setRotationX(bone2.getRotationX() - MathHelper.cos(value * 0.09F) * 0.05F + 0.05F);
    }

}

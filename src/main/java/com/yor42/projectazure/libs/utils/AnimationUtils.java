package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CrossbowItem;
import software.bernie.geckolib3.core.processor.IBone;

public class AnimationUtils {

    public static void GeckolibanimateCrossbowHold(IBone p_239104_0_, IBone p_239104_1_, IBone head, boolean p_239104_3_) {
        IBone modelrenderer = p_239104_3_ ? p_239104_0_ : p_239104_1_;
        IBone modelrenderer1 = p_239104_3_ ? p_239104_1_ : p_239104_0_;
        modelrenderer.setRotationY((p_239104_3_ ? 0.3F : -0.3F) + head.getRotationY());
        modelrenderer1.setRotationY((p_239104_3_ ? -0.6F : 0.6F) + head.getRotationY());
        modelrenderer.setRotationX(((float)Math.PI / 2F) + head.getRotationX() + 0.1F);
        modelrenderer1.setRotationX(1.5F + head.getRotationX());
    }

    public static void SwingArm(IBone Left, IBone Right, IBone chest, IBone head, AbstractEntityCompanion p_230486_1_, float p_230486_2_) {
        float attacktime = p_230486_1_.getAttackAnim(p_230486_2_);
        if (!(attacktime <= 0.0F)) {
            HumanoidArm handside = getAttackArm(p_230486_1_);
            IBone modelrenderer = getArm(Right, Left, handside);
            float f = attacktime;
            chest.setRotationY(Mth.sin(Mth.sqrt(f) * ((float) Math.PI * 2F)) * -0.2F);
            if (handside == HumanoidArm.LEFT) {
                chest.setRotationY(chest.getRotationY()*-1);
            }

            Right.setRotationY(Right.getRotationY()+chest.getRotationY());
            Left.setRotationY(Left.getRotationY()+chest.getRotationY());
            Left.setRotationY(Left.getRotationY()+chest.getRotationY());
            f = 1.0F - attacktime;
            f = f * f;
            f = f * f;
            f = 1.0F - f;
            float f1 = Mth.sin(f * (float) Math.PI);
            float f2 = Mth.sin(attacktime * (float) Math.PI) * -(head.getRotationX() - 0.7F) * 0.75F;
            modelrenderer.setRotationX((float) ((double) modelrenderer.getRotationX() - ((double) f1 * 1.2D + (double) f2))*-1);
            modelrenderer.setRotationY(modelrenderer.getRotationY()+chest.getRotationY() * 2.0F*-1);
            modelrenderer.setPositionZ(modelrenderer.getRotationZ()+Mth.sin(attacktime * (float) Math.PI) * 0.4F);
        }
    }

    private static IBone getArm(IBone right, IBone left, HumanoidArm handside) {
        return handside == HumanoidArm.RIGHT? right:left;
    }

    protected static HumanoidArm getAttackArm(AbstractEntityCompanion p_217147_1_) {
        HumanoidArm handside = p_217147_1_.getMainArm();
        return p_217147_1_.swingingArm == InteractionHand.MAIN_HAND ? handside : handside.getOpposite();
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

    public static <T extends Mob> void GeckolibswingWeaponDown(IBone bone1, IBone bone2, T mob, float p_239103_3_, float p_239103_4_) {
        float f = Mth.sin(p_239103_3_ * (float)Math.PI);
        float f1 = Mth.sin((1.0F - (1.0F - p_239103_3_) * (1.0F - p_239103_3_)) * (float)Math.PI);
        bone1.setRotationZ(0.0F);
        bone2.setRotationZ(0.0F);
        bone1.setRotationY(0.15707964F);
        bone2.setRotationY(-0.15707964F);
        if (mob.getMainArm() == HumanoidArm.RIGHT) {
            bone1.setRotationX(-1.8849558F + Mth.cos(p_239103_4_ * 0.09F) * 0.15F);
            bone2.setRotationX(-0.0F + Mth.cos(p_239103_4_ * 0.19F) * 0.5F);
            bone1.setRotationX(bone1.getRotationX() + f * 2.2F - f1 * 0.4F);
            bone2.setRotationX(bone2.getRotationX() + f * 1.2F - f1 * 0.4F);
        } else {
            bone1.setRotationX(-0.0F + Mth.cos(p_239103_4_ * 0.19F) * 0.5F);
            bone2.setRotationX(-1.8849558F + Mth.cos(p_239103_4_ * 0.09F) * 0.15F);
            bone1.setRotationX(bone1.getRotationX()+f * 1.2F - f1 * 0.4F);
            bone2.setRotationX(bone1.getRotationX()+f * 2.2F - f1 * 0.4F);
        }

        GeckolibbobArms(bone1, bone2, p_239103_4_);
    }

    public static void GeckolibbobArms(IBone bone1, IBone bone2, float value) {
        bone1.setRotationZ(bone1.getRotationZ() + Mth.cos(value * 0.09F) * 0.05F + 0.05F);
        bone2.setRotationZ(bone2.getRotationZ() - Mth.cos(value * 0.09F) * 0.05F + 0.05F);
        bone1.setRotationX(bone1.getRotationX() + Mth.cos(value * 0.09F) * 0.05F + 0.05F);
        bone2.setRotationX(bone2.getRotationX() - Mth.cos(value * 0.09F) * 0.05F + 0.05F);
    }

}

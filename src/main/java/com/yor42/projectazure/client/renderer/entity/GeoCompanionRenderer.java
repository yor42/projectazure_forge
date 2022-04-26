package com.yor42.projectazure.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ChatFormatting;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public abstract class GeoCompanionRenderer<T extends LivingEntity & IAnimatable> extends GeoEntityRenderer<T> {
    protected GeoCompanionRenderer(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
    }

    @Override
    protected void applyRotations(T entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        Pose pose = entityLiving.getPose();
        if (pose != Pose.SLEEPING) {
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
        }

        //PA Start: Do not rotate model when companion is dead.
        if (entityLiving.isAutoSpinAttack()) {
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(-90.0F - entityLiving.xRot));
            matrixStackIn
                    .mulPose(Vector3f.YP.rotationDegrees(((float) entityLiving.tickCount + partialTicks) * -75.0F));
        } else if (pose == Pose.SLEEPING) {
            Direction direction = entityLiving.getBedOrientation();
            float f1 = direction != null ? getFacingAngle(direction) : rotationYaw;
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f1));
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(this.getDeathMaxRotation(entityLiving)));
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(270.0F));
        } else if (entityLiving.hasCustomName() || entityLiving instanceof PlayerEntity) {
            String s = ChatFormatting.stripFormatting(entityLiving.getName().getString());
            if (("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof PlayerEntity)
                    || ((PlayerEntity) entityLiving).isModelPartShown(PlayerModelPart.CAPE))) {
                matrixStackIn.translate(0.0D, (double) (entityLiving.getBbHeight() + 0.1F), 0.0D);
                matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            }
        }
        //PA end;
    }

    private static float getFacingAngle(Direction facingIn) {
        switch (facingIn) {
            case SOUTH:
                return 90.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }
}

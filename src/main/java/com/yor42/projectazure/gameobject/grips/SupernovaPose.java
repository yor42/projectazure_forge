package com.yor42.projectazure.gameobject.grips;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.tac.guns.client.handler.ReloadHandler;
import com.tac.guns.client.render.pose.MiniGunPose;
import com.tac.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class SupernovaPose extends MiniGunPose {

    @Override
    public void renderFirstPersonArms(LocalPlayer player, HumanoidArm hand, ItemStack stack, PoseStack matrixStack, MultiBufferSource buffer, int light, float partialTicks) {
        matrixStack.translate(0, 0, -1);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180F));

        matrixStack.pushPose();

        float reloadProgress = ReloadHandler.get().getReloadProgress(partialTicks, stack);
        matrixStack.translate(reloadProgress * 1.5, -reloadProgress, -reloadProgress * 1.5);


        int side = hand.getOpposite() == HumanoidArm.RIGHT ? 1 : -1;
        matrixStack.translate(7.0 * side * 0.0625, -0.995, -0.2);

        if (Minecraft.getInstance().player.getModelName().equals("slim") && hand.getOpposite() == HumanoidArm.LEFT) {
            matrixStack.translate(0.03125F * -side, 0, 0);
        }

        matrixStack.mulPose(Vector3f.XP.rotationDegrees(120F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(50F * -side));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(5F * -side));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-40F));
        matrixStack.translate(0.1F, -0.8F, -0.4F);
        matrixStack.scale(0.8F,0.8F,0.8F);

        RenderUtil.renderFirstPersonArm(player, hand.getOpposite(), matrixStack, buffer, light);

        matrixStack.popPose();

        double centerOffset = 2.5;
        if (Minecraft.getInstance().player.getModelName().equals("slim")) {
            centerOffset += hand == HumanoidArm.RIGHT ? 0.2 : 0.8;
        }
        centerOffset = hand == HumanoidArm.RIGHT ? -centerOffset : centerOffset;
        matrixStack.translate(centerOffset * 0.0465, -0.4, -0.975);

        matrixStack.mulPose(Vector3f.XP.rotationDegrees(80F));
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        RenderUtil.renderFirstPersonArm(player, hand, matrixStack, buffer, light);
    }

}

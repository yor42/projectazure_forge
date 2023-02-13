package com.yor42.projectazure.intermod.curios.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderDefib extends CurioRenderer {

    private static RenderDefib RENDERER_INSTANCE = new RenderDefib();

    public static RenderDefib getRendererInstance() {
        return RENDERER_INSTANCE;
    }

    public void render(ItemStack stack, LivingEntity entity, MatrixStack posestack, IRenderTypeBuffer buffer, int light, float partialTicks) {
        posestack.pushPose();
        if(entity.isCrouching()) {
            posestack.translate(0, 0.1, -0.1);
            posestack.mulPose(Vector3f.XP.rotation(MathUtil.DegreeToRadian(90F / (float) Math.PI)));
        }

        posestack.pushPose();
        posestack.translate(0, 0.4, 0.1);
        posestack.mulPose(Vector3f.XP.rotationDegrees(90));
        posestack.mulPose(Vector3f.YP.rotationDegrees(180));
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.GROUND, light, OverlayTexture.NO_OVERLAY, posestack, buffer);
        posestack.popPose();

        posestack.popPose();
    }

}

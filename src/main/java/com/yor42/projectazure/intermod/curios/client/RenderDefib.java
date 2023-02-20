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
public class RenderDefib implements ICurioRenderer {

    private static final RenderDefib RENDERER_INSTANCE = new RenderDefib();

    public static RenderDefib getRendererInstance() {
        return RENDERER_INSTANCE;
    }

    public void render(ItemStack stack, String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStack.pushPose();
        if(livingEntity.isCrouching()) {
            matrixStack.translate(0, 0.1, -0.1);
            matrixStack.mulPose(Vector3f.XP.rotation(MathUtil.DegreeToRadian(90F / (float) Math.PI)));
        }

        matrixStack.pushPose();
        matrixStack.translate(0, 0.4, 0.1);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.GROUND, light, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer);
        matrixStack.popPose();

        matrixStack.popPose();
    }

}

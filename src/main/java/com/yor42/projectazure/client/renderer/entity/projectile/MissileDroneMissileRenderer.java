package com.yor42.projectazure.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yor42.projectazure.client.model.entity.misc.modelProjectileDroneMissile;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityMissileDroneMissile;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class MissileDroneMissileRenderer extends EntityRenderer<EntityMissileDroneMissile> {

    private static final ResourceLocation TEXTURE = ModResourceLocation("textures/entity/projectile/missiledrone_missile.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);

    public MissileDroneMissileRenderer(EntityRendererProvider.Context p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMissileDroneMissile entityMissileDroneMissile) {
        return TEXTURE;
    }

    @Override
    public void render(EntityMissileDroneMissile entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        Model model = new modelProjectileDroneMissile();
        VertexConsumer builder = bufferIn.getBuffer(RENDER_TYPE);
        matrixStackIn.pushPose();
        matrixStackIn.translate(0,-0.7,0);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0F));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
        matrixStackIn.scale(0.5F,0.5F,0.5F);
        model.renderToBuffer(matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0F);
        matrixStackIn.popPose();
    }
}

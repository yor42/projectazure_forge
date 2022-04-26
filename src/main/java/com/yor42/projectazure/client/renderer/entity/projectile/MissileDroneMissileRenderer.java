package com.yor42.projectazure.client.renderer.entity.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yor42.projectazure.client.model.entity.misc.modelProjectileDroneMissile;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityMissileDroneMissile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class MissileDroneMissileRenderer extends EntityRenderer<EntityMissileDroneMissile> {

    private static final ResourceLocation TEXTURE = ModResourceLocation("textures/entity/projectile/missiledrone_missile.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);

    public MissileDroneMissileRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMissileDroneMissile entityMissileDroneMissile) {
        return TEXTURE;
    }

    @Override
    public void render(EntityMissileDroneMissile entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        Model model = new modelProjectileDroneMissile();
        VertexConsumer  builder = bufferIn.getBuffer(RENDER_TYPE);
        matrixStackIn.pushPose();
        matrixStackIn.translate(0,-0.7,0);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.yRotO, entityIn.yRot) - 90.0F));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.xRotO, entityIn.xRot)));
        matrixStackIn.scale(0.5F,0.5F,0.5F);
        model.renderToBuffer(matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0F);
        matrixStackIn.popPose();
    }
}

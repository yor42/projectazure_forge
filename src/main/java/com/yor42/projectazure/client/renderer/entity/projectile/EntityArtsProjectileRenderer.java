package com.yor42.projectazure.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yor42.projectazure.client.model.entity.misc.ModelArtsProjectile;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityArtsProjectile;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class EntityArtsProjectileRenderer extends EntityRenderer<EntityArtsProjectile> {

    private static final ResourceLocation TEXTURE = ModResourceLocation("textures/entity/projectile/artsprojectile.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);

    public EntityArtsProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityArtsProjectile entity) {
        return TEXTURE;
    }

    @Override
    public void render(@Nonnull EntityArtsProjectile entityIn, float entityYaw, float partialTicks, @Nonnull PoseStack matrixStackIn, @Nonnull MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        EntityModel<EntityArtsProjectile> model = new ModelArtsProjectile();
        VertexConsumer builder = bufferIn.getBuffer(RENDER_TYPE);
        matrixStackIn.translate(0,-1,0);
        matrixStackIn.pushPose();
        float f = Mth.rotLerp(partialTicks, entityIn.yRotO, entityIn.getYRot());
        float f6 = Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot());
        model.setupAnim(entityIn,partialTicks,0.0F, -0.1F, f, f6);
        model.renderToBuffer(matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0F);
        matrixStackIn.popPose();
    }
}

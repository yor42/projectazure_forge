package com.yor42.projectazure.client.renderer.entity.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.entity.misc.ModelProjectileFallingnSword;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityFallingSword;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class EntityFallingSwordRenderer extends EntityRenderer<EntityFallingSword> {

    private static final ResourceLocation TEXTURE = ModResourceLocation("textures/item/flexsword.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);


    public EntityFallingSwordRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityFallingSword p_110775_1_) {
        return TEXTURE;
    }

    @Override
    public void render(@Nonnull EntityFallingSword entityIn, float entityYaw, float partialTicks, @Nonnull MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        EntityModel<EntityFallingSword> model = new ModelProjectileFallingnSword();
        IVertexBuilder builder = bufferIn.getBuffer(RENDER_TYPE);
        matrixStackIn.pushPose();
        float f = MathHelper.rotLerp(partialTicks, entityIn.yRotO, entityIn.yRot);
        float f6 = MathHelper.lerp(partialTicks, entityIn.xRotO, entityIn.xRot);
        model.setupAnim(entityIn,partialTicks,0.0F, -0.1F, f, f6);
        model.renderToBuffer(matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0F);
        matrixStackIn.popPose();
    }
}

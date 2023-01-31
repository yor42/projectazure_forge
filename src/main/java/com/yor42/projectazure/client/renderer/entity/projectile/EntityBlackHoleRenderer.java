package com.yor42.projectazure.client.renderer.entity.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityCausalBlackhole;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class EntityBlackHoleRenderer extends EntityRenderer<EntityCausalBlackhole> {

    private static final ResourceLocation TEXTURE = ModResourceLocation("textures/entity/projectile/blackhole.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(TEXTURE);

    public EntityBlackHoleRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public void render(EntityCausalBlackhole p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int packedLightIn) {
        p_225623_4_.pushPose();
        p_225623_4_.translate(0,1.5, 0);
        p_225623_4_.scale(3,3,3);
        p_225623_4_.mulPose(this.entityRenderDispatcher.cameraOrientation());
        //p_225623_4_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        p_225623_4_.mulPose(Vector3f.ZP.rotation((float) ((2*Math.PI)*(((float)p_225623_1_.tickCount%200F) /200F))));
        MatrixStack.Entry matrixstack$entry = p_225623_4_.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        IVertexBuilder ivertexbuilder = p_225623_5_.getBuffer(RENDER_TYPE);
        vertex(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0F, 0, 0, 1);
        vertex(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0F, 0, 1, 1);
        vertex(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0F, 1, 1, 0);
        vertex(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0F, 1, 0, 0);
        p_225623_4_.popPose();
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCausalBlackhole p_110775_1_) {
        return TEXTURE;
    }

    private static void vertex(IVertexBuilder builder, Matrix4f matrix, Matrix3f p_229045_2_, int p_229045_3_, float p_229045_4_, int p_229045_5_, int p_229045_6_, int p_229045_7_) {
        builder.vertex(matrix, p_229045_4_ - 0.5F, (float)p_229045_5_ - 0.5F, 0.0F).color(255, 255, 255, 255).uv((float)p_229045_6_, (float)p_229045_7_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229045_3_).normal(p_229045_2_, 0.0F, 1.0F, 0.0F).endVertex();
    }
}

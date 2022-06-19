package com.yor42.projectazure.client.renderer.entity.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityCannonPelllet;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class entityCannonPelletRenderer extends EntityRenderer<EntityCannonPelllet> {

    private static final ResourceLocation TEXTURE = ModResourceLocation("textures/entity/projectile/shell_generic.png");
    private static final RenderType RENDER_TYPE = RenderType.entitySmoothCutout(TEXTURE);

    public entityCannonPelletRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(EntityCannonPelllet entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        packedLightIn = LightTexture.pack(15, 15);
        matrixStackIn.pushPose();
        float xrot = entityIn.xRot;
        float yrot = entityYaw;
        float zrot = this.entityRenderDispatcher.cameraOrientation().k()*this.entityRenderDispatcher.cameraOrientation().r();
        float vec = Math.max(1,Math.max(Math.max(Math.abs(xrot), Math.abs(yrot)), Math.abs(zrot)));
        xrot/=vec;
        yrot/=vec;
        zrot/=vec;
        Quaternion camerarot = new Quaternion(new Vector3f(xrot, yrot*-1, zrot*-1), vec, true);
        matrixStackIn.mulPose(camerarot);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        MatrixStack.Entry matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RENDER_TYPE);
        vertex(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0F, 0, 0, 1);
        vertex(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0F, 0, 1, 1);
        vertex(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0F, 1, 1, 0);
        vertex(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0F, 1, 0, 0);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private static void vertex(IVertexBuilder builder, Matrix4f p_229045_1_, Matrix3f p_229045_2_, int p_229045_3_, float p_229045_4_, int p_229045_5_, int p_229045_6_, int p_229045_7_) {
        builder.vertex(p_229045_1_, p_229045_4_ - 0.5F, (float)p_229045_5_ - 0.25F, 0.0F).color(255, 255, 255, 255).uv((float)p_229045_6_, (float)p_229045_7_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229045_3_).normal(p_229045_2_, 0.0F, 1.0F, 0.0F).endVertex();
    }


    @Override
    public ResourceLocation getTextureLocation(EntityCannonPelllet entity) {
        return TEXTURE;
    }
}

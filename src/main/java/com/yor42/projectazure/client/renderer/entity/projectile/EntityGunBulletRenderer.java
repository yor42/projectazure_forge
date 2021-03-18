package com.yor42.projectazure.client.renderer.entity.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.entity.misc.modelProjectileGunBullet;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileBullet;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class EntityGunBulletRenderer extends EntityRenderer<EntityProjectileBullet> {

    private static final ResourceLocation TEXTURE = ModResourceLocation("textures/entity/projectile/gun_bullet.png");
    private static final RenderType RENDER_TYPE = RenderType.getEntityCutoutNoCull(TEXTURE);

    public EntityGunBulletRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(EntityProjectileBullet entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        Model model = new modelProjectileGunBullet();
        IVertexBuilder builder = bufferIn.getBuffer(RENDER_TYPE);
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw)));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch)));
        model.render(matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0F);
        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityProjectileBullet entity) {
        return TEXTURE;
    }
}

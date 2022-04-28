package com.yor42.projectazure.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yor42.projectazure.client.model.entity.misc.ModelArtsProjectile;
import com.yor42.projectazure.client.model.entity.misc.modelProjectileDroneMissile;
import com.yor42.projectazure.client.model.entity.misc.modelProjectileGunBullet;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityArtsProjectile;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityMissileDroneMissile;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileBullet;
import com.yor42.projectazure.setup.register.RegisterModelLayers;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class EntityGunBulletRenderer extends EntityRenderer<EntityProjectileBullet> {
    private final EntityRendererProvider.Context ctx;
    private static final ResourceLocation TEXTURE = ModResourceLocation("textures/entity/projectile/gun_bullet.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);
    public EntityGunBulletRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
        this.ctx = renderManager;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityProjectileBullet entity) {
        return TEXTURE;
    }

    @Override
    public void render(@Nonnull EntityProjectileBullet entityIn, float entityYaw, float partialTicks, @Nonnull PoseStack matrixStackIn, @Nonnull MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

        Model model = new modelProjectileGunBullet(this.ctx.bakeLayer(RegisterModelLayers.FIREARM_BULLET));
        VertexConsumer  builder = bufferIn.getBuffer(RENDER_TYPE);
        matrixStackIn.pushPose();
        matrixStackIn.translate(0, -1.2, 0);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0F));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
        model.renderToBuffer(matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0F);
        matrixStackIn.popPose();
    }
}

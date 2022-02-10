package com.yor42.projectazure.client.renderer.entity.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.client.model.entity.misc.ModelKnife;
import com.yor42.projectazure.client.renderer.GeoProjectileRenderer;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityThrownKnifeProjectile;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class EntityThrownKnifeRenderer extends GeoProjectileRenderer<EntityThrownKnifeProjectile> {

    public EntityThrownKnifeRenderer(EntityRendererManager renderManager){
        super(renderManager, new ModelKnife());
    }

    @Override
    public void render(EntityThrownKnifeProjectile entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.yRotO, entity.yRot) - 90.0F));
        stack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entity.xRotO, entity.xRot) + 90.0F));
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }
}

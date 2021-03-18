package com.yor42.projectazure.client.renderer.entity.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.client.model.entity.misc.modelProjectileTorpedo;
import com.yor42.projectazure.client.renderer.GeoProjectileRenderer;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileTorpedo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class EntityProjectileTorpedoRenderer extends GeoProjectileRenderer<EntityProjectileTorpedo> {
    public EntityProjectileTorpedoRenderer(EntityRendererManager renderManager) {
        super(renderManager, new modelProjectileTorpedo());
        this.shadowSize = 0.7F; //change 0.7 to the desired shadow size.
    }

    @Override
    public void render(EntityProjectileTorpedo entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.scale(0.4F, 0.4F, 0.4F);
        matrixStackIn.translate(0,0.75,0);
        matrixStackIn.rotate(new Quaternion(Vector3f.XP, entityYaw, false));
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, entityIn.rotationPitch, false));
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}

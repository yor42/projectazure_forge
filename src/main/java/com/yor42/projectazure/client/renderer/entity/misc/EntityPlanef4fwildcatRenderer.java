package com.yor42.projectazure.client.renderer.entity.misc;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yor42.projectazure.client.model.planes.modelEntityPlaneF4FWildCat;
import com.yor42.projectazure.gameobject.entity.misc.EntityF4fWildcat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class EntityPlanef4fwildcatRenderer extends GeoEntityRenderer<EntityF4fWildcat> {
    private EntityF4fWildcat entity;

    public EntityPlanef4fwildcatRenderer(EntityRendererManager renderManager) {
        super(renderManager, new modelEntityPlaneF4FWildCat());
    }

    @Override
    public void renderEarly(EntityF4fWildcat animatable, MatrixStack stackIn, float ticks, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer  vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.entity = animatable;
    }

    @Override
    public void render(EntityF4fWildcat entity, float entityYaw, float partialTicks, MatrixStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.scale(0.4F, 0.4F, 0.4F);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityF4fWildcat entity) {
        return ModResourceLocation("textures/planes/f4f_wildcat.png");
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, VertexConsumer  bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        super.renderRecursively(bone,stack,bufferIn,packedLightIn,packedOverlayIn,red,green,blue,alpha);
    }
}

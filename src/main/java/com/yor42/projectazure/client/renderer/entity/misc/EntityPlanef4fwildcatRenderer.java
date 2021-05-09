package com.yor42.projectazure.client.renderer.entity.misc;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.planes.modelEntityPlaneF4FWildCat;
import com.yor42.projectazure.gameobject.entity.misc.EntityF4fWildcat;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

public class EntityPlanef4fwildcatRenderer extends GeoEntityRenderer<EntityF4fWildcat> {
    private EntityF4fWildcat entity;

    public EntityPlanef4fwildcatRenderer(EntityRendererManager renderManager) {
        super(renderManager, new modelEntityPlaneF4FWildCat());
    }

    @Override
    public void renderEarly(EntityF4fWildcat animatable, MatrixStack stackIn, float ticks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.entity = animatable;
    }

    @Override
    public void render(EntityF4fWildcat entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        stack.scale(0.4F, 0.4F, 0.4F);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        super.renderRecursively(bone,stack,bufferIn,packedLightIn,packedOverlayIn,red,green,blue,alpha);
    }
}

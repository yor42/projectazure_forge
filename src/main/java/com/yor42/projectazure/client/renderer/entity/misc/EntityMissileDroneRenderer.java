package com.yor42.projectazure.client.renderer.entity.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yor42.projectazure.client.model.planes.ModelMissileDroneEntity;
import com.yor42.projectazure.gameobject.entity.misc.EntityMissileDrone;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class EntityMissileDroneRenderer extends GeoEntityRenderer<EntityMissileDrone> {

    int ammo;

    public EntityMissileDroneRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelMissileDroneEntity());
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMissileDrone entity) {
        return ModResourceLocation("textures/planes/missiledrone.png");
    }

    @Override
    public void renderEarly(EntityMissileDrone animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer  vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.ammo = animatable.getammo();
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    @Override
    public void render(GeoModel model, EntityMissileDrone animatable, float partialTicks, RenderType type, PoseStack matrixStackIn, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer  vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStackIn.pushPose();
        matrixStackIn.scale(0.5F,0.5F,0.5F);
        super.render(model, animatable, partialTicks, type, matrixStackIn, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrixStackIn.popPose();
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer  bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        String name = bone.getName();
        if(name.contains("missile")){
            int missilenumber = Integer.parseInt(name.replaceAll("[^0-9]", ""));
            bone.setHidden(!(missilenumber<=this.ammo));
        }

        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}

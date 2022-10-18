package com.yor42.projectazure.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.AnimationUtils;

import javax.annotation.Nullable;
import java.util.Collections;

public class GeoProjectileRenderer <T extends Entity & IAnimatable> extends EntityRenderer<T>
        implements IGeoRenderer<T> {

    public IRenderTypeBuffer rtb;

    static {
        AnimationController.addModelFetcher((IAnimatable object) -> {
            if (object instanceof Entity) {
                return (IAnimatableModel<Object>) AnimationUtils.getGeoModelForEntity((Entity) object);
            }
            return null;
        });
    }

    protected final AnimatedGeoModel<T> modelProvider;

    protected GeoProjectileRenderer(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager);
        this.modelProvider = modelProvider;
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int packedLightIn) {
        EntityModelData entityModelData = new EntityModelData();
        GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(entityIn));
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(
                MathHelper.lerp(partialTicks, entityIn.yRotO, entityIn.yRot) - 180.0F));
        matrixStackIn.mulPose(Vector3f.ZP
                .rotationDegrees(MathHelper.lerp(partialTicks, entityIn.xRotO, entityIn.xRot)));
        Minecraft.getInstance().textureManager.bind(getTextureLocation(entityIn));
        AnimationEvent<T> predicate = new AnimationEvent<T>(entityIn, 0, 0, partialTicks,
                !entityIn.getDeltaMovement().equals(Vector3d.ZERO), Collections.singletonList(entityModelData));
        ((IAnimatableModel<T>) modelProvider).setLivingAnimations(entityIn, this.getUniqueID(entityIn), predicate);
        Color renderColor = getRenderColor(entityIn, partialTicks, matrixStackIn, bufferIn, null, packedLightIn);
        RenderType renderType = getRenderType(entityIn, partialTicks, matrixStackIn, bufferIn, null, packedLightIn,
                getTextureLocation(entityIn));
        render(model, entityIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn,
                getPackedOverlay(entityIn, 0), (float) renderColor.getRed() / 255f,
                (float) renderColor.getBlue() / 255f, (float) renderColor.getGreen() / 255f,
                (float) renderColor.getAlpha() / 255);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public static int getPackedOverlay(Entity livingEntityIn, float uIn) {
        return OverlayTexture.pack(OverlayTexture.u(uIn), OverlayTexture.v(false));
    }

    @Override
    public void setCurrentRTB(IRenderTypeBuffer rtb) {
        this.rtb = rtb;
    }

    @Override
    public void renderEarly(T animatable, MatrixStack stackIn, float partialTicks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        IGeoRenderer.super.renderEarly(animatable, stackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rtb = renderTypeBuffer;
    }

    @Override
    public IRenderTypeBuffer getCurrentRTB() {
        return this.rtb;
    }

    @Override
    public GeoModelProvider<T> getGeoModelProvider() {
        return this.modelProvider;
    }

    @Override
    public ResourceLocation getTextureLocation(T instance) {
        return this.modelProvider.getTextureLocation(instance);
    }

}

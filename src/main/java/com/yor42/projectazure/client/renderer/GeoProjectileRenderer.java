package com.yor42.projectazure.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.AnimationUtils;

import java.awt.*;

public class GeoProjectileRenderer <T extends Entity & IAnimatable> extends EntityRenderer<T>
        implements IGeoRenderer<T> {

    static {
        AnimationController.addModelFetcher((IAnimatable object) -> {
            if (object instanceof Entity) {
                return (IAnimatableModel<Object>) AnimationUtils.getGeoModelForEntity((Entity) object);
            }
            return null;
        });
    }

    private final AnimatedGeoModel<T> modelProvider;

    protected GeoProjectileRenderer(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager);
        this.modelProvider = modelProvider;
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int packedLightIn) {
        GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(entityIn));
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(
                MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw) - 90.0F));
        matrixStackIn.rotate(Vector3f.ZP
                .rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch)));
        Minecraft.getInstance().textureManager.bindTexture(getEntityTexture(entityIn));
        Color renderColor = getRenderColor(entityIn, partialTicks, matrixStackIn, bufferIn, null, packedLightIn);
        RenderType renderType = getRenderType(entityIn, partialTicks, matrixStackIn, bufferIn, null, packedLightIn,
                getEntityTexture(entityIn));
        render(model, entityIn, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn,
                getPackedOverlay(entityIn, 0), (float) renderColor.getRed() / 255f,
                (float) renderColor.getBlue() / 255f, (float) renderColor.getGreen() / 255f,
                (float) renderColor.getAlpha() / 255);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public static int getPackedOverlay(Entity livingEntityIn, float uIn) {
        return OverlayTexture.getPackedUV(OverlayTexture.getU(uIn), OverlayTexture.getV(false));
    }

    @Override
    public GeoModelProvider<T> getGeoModelProvider() {
        return this.modelProvider;
    }

    @Override
    public ResourceLocation getTextureLocation(T instance) {
        return this.modelProvider.getTextureLocation(instance);
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return getTextureLocation(entity);
    }

}

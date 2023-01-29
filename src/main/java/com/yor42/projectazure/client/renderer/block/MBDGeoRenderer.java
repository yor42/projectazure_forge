package com.yor42.projectazure.client.renderer.block;

import com.lowdragmc.multiblocked.client.renderer.impl.GeoComponentRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import software.bernie.geckolib3.geo.render.built.GeoBone;

public class MBDGeoRenderer extends GeoComponentRenderer {

    private final String texturename;
    public MBDGeoRenderer(String modelName, String texturename, boolean isGlobal) {
        super(modelName, isGlobal);
        this.texturename = texturename;
    }

    public MBDGeoRenderer(String modelName, boolean isGlobal) {
        this(modelName, modelName, isGlobal);
    }

    @OnlyIn(Dist.CLIENT)
    public void onTextureSwitchEvent(TextureStitchEvent.Pre event) {
        event.addSprite(new ResourceLocation(Constants.MODID, this.texturename));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.name.contains("emissive")) {
            packedLightIn = 15728880;
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public ResourceLocation getAnimationFileLocation(GeoComponentRenderer.ComponentFactory entity) {
        return new ResourceLocation(Constants.MODID, String.format("animations/block/machine/%s.animation.json", this.texturename));
    }

    public ResourceLocation getModelLocation(GeoComponentRenderer.ComponentFactory animatable) {
        return new ResourceLocation(Constants.MODID, String.format("geo/block/%s.geo.json", this.modelName));
    }

    public ResourceLocation getTextureLocation(GeoComponentRenderer.ComponentFactory entity) {
        return new ResourceLocation(Constants.MODID, String.format("textures/block/%s.png", this.texturename));
    }
}

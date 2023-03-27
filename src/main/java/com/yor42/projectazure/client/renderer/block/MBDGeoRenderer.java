package com.yor42.projectazure.client.renderer.block;

import com.lowdragmc.multiblocked.client.renderer.impl.GeoComponentRenderer;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;

import javax.annotation.Nonnull;

public class MBDGeoRenderer extends GeoComponentRenderer {
    public final String modelName, texturename;

    public MBDGeoRenderer(String modelName, String texturename, boolean isGlobal) {
        super(modelName, isGlobal);
        this.modelName = modelName;
        this.texturename = texturename;
    }

    public MBDGeoRenderer(String modelName, boolean isGlobal) {
        super(modelName, isGlobal);
        this.modelName = modelName;
        this.texturename = modelName;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onTextureSwitchEvent(TextureStitchEvent.Pre event) {
        event.addSprite(new ResourceLocation(Constants.MODID, modelName));
    }

    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public TextureAtlasSprite getParticleTexture() {
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation(Constants.MODID, modelName));
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ComponentFactory entity) {
        return new ResourceLocation(Constants.MODID, String.format("animations/block/machine/%s.animation.json", modelName));
    }

    @Override
    public ResourceLocation getModelLocation(ComponentFactory animatable) {
        return new ResourceLocation(Constants.MODID, String.format("geo/block/%s.geo.json", modelName));
    }

    @Override
    public ResourceLocation getTextureLocation(ComponentFactory entity) {
        return new ResourceLocation(Constants.MODID, String.format("textures/block/%s.png", texturename));
    }
}

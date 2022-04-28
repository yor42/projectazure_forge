package com.yor42.projectazure.client.model.block;

import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityMetalPress;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelBlockPress extends AnimatedGeoModel<TileEntityMetalPress> {
    @Override
    public ResourceLocation getModelLocation(TileEntityMetalPress tileEntityMetalPress) {
        return ResourceUtils.ModelLocation("block/blockmetalpress.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TileEntityMetalPress tileEntityMetalPress) {
        return ResourceUtils.ModResourceLocation("textures/block/metal_press.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TileEntityMetalPress tileEntityMetalPress) {
        return ResourceUtils.AnimationLocation("block/machine/metal_press.animation.json");
    }
}

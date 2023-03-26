package com.yor42.projectazure.client.renderer.block;

import com.yor42.projectazure.client.model.block.ModelBlockPress;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityMetalPress;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class MachineMetalPressRenderer extends GeoBlockRenderer<TileEntityMetalPress> {
    public MachineMetalPressRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new ModelBlockPress());
    }
}

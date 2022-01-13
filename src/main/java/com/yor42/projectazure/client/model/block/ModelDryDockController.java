package com.yor42.projectazure.client.model.block;

import com.yor42.projectazure.gameobject.blocks.blockMultiblockDryDockController;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockDrydockTE;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.yor42.projectazure.gameobject.blocks.AbstractMultiBlockBase.FORMED;

public class ModelDryDockController extends AnimatedGeoModel<MultiblockDrydockTE> {
    @Override
    public ResourceLocation getModelLocation(MultiblockDrydockTE TE) {

        if(TE.getLevel() != null) {
            BlockState state = TE.getLevel().getBlockState(TE.getBlockPos());
            if (state.getBlock() instanceof blockMultiblockDryDockController && state.hasProperty(FORMED) &&  state.getValue(FORMED)) {
                return ResourceUtils.ModelLocation("block/drydock_built.geo.json");
            }
            else {
                return ResourceUtils.ModelLocation("block/drydock_controller.geo.json");
            }
        }
        return ResourceUtils.ModelLocation("block/drydock_controller.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(MultiblockDrydockTE TE) {

        if(TE.getLevel() != null) {
            BlockState state = TE.getLevel().getBlockState(TE.getBlockPos());
            if (state.getBlock() instanceof blockMultiblockDryDockController && state.hasProperty(FORMED) && state.getValue(FORMED)) {
                return ResourceUtils.ModResourceLocation("textures/block/drydock_controller_built.png");
            }
        }

        return ResourceUtils.ModResourceLocation("textures/block/drydock_controller.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(MultiblockDrydockTE TE) {
        return ResourceUtils.AnimationLocation("block/machine/drydock.animation.json");
    }
}

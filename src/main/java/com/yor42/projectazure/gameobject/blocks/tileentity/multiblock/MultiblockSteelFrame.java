package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.yor42.projectazure.setup.register.registerTE.MULTIBLOCKSTRUCTURE_STEELFRAME;

public class MultiblockSteelFrame extends MultiblockStructuralBlockTE{
    public MultiblockSteelFrame(BlockPos pos, BlockState state) {
        super(MULTIBLOCKSTRUCTURE_STEELFRAME.get(), pos, state);
    }
}

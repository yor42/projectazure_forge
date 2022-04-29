package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.yor42.projectazure.Main.MULTIBLOCKSTRUCTURE_STEELFRAME_BLOCK_ENTITY;

public class MultiblockSteelFrame extends MultiblockStructuralBlockTE{
    public MultiblockSteelFrame(BlockPos pos, BlockState state) {
        super(MULTIBLOCKSTRUCTURE_STEELFRAME_BLOCK_ENTITY.get(), pos, state);
    }
}

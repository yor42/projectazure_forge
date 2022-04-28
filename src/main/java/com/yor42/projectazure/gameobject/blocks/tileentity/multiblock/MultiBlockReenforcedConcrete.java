package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.yor42.projectazure.setup.register.registerTE.MULTIBLOCKSTRUCTURE_CONCRETE;

public class MultiBlockReenforcedConcrete extends MultiblockStructuralBlockTE {
    public MultiBlockReenforcedConcrete(BlockPos pos, BlockState state) {
        super(MULTIBLOCKSTRUCTURE_CONCRETE.get(), pos, state);
    }
}

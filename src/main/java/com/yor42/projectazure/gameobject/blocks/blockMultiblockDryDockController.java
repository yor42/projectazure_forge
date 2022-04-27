package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockDrydockTE;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.world.PathNavigationRegion;

import javax.annotation.Nullable;

public class blockMultiblockDryDockController extends MultiblockStructureBlocks{

    public blockMultiblockDryDockController(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, PathNavigationRegion world) {
        return new MultiblockDrydockTE();
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}

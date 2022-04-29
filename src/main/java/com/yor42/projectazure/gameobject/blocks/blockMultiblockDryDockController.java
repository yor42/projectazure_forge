package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockDrydockTE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class blockMultiblockDryDockController extends MultiblockStructureBlocks{

    public blockMultiblockDryDockController(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MultiblockDrydockTE(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return p_153214_ == Main.DRYDOCK_BLOCK_ENTITY.get() ? MultiblockDrydockTE::tick : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}

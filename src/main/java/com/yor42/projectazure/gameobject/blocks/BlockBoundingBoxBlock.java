package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityBoundingBox;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockBaseTE;
import com.yor42.projectazure.libs.utils.MultiblockHandler;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class BlockBoundingBoxBlock extends AbstractContainerBlock{
    public BlockBoundingBoxBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public boolean shouldNotifyBlockChange() {
        return true;
    }

    @Override
    public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canBeReplacedByLogs(BlockState state, IWorldReader world, BlockPos pos) {
        return false;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tile = worldIn.getTileEntity(pos);

        if(!worldIn.isRemote && tile instanceof TileEntityBoundingBox) {
            TileEntityBoundingBox tile2 = (TileEntityBoundingBox) tile;

            if (tile2.hasMaster()) {
                worldIn.destroyBlock(tile2.getMasterPos(), true, player);
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        TileEntity tile = world.getTileEntity(pos);

        if(!world.isRemote && tile instanceof TileEntityBoundingBox) {
            TileEntityBoundingBox tile2 = (TileEntityBoundingBox) tile;

            if (tile2.hasMaster()) {
                world.destroyBlock(tile2.getMasterPos(), true, null);
            }
        }

    }
}

package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockBaseTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockDrydockTE;
import com.yor42.projectazure.libs.utils.MultiblockHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class AbstractMultiBlockBase extends AbstractAnimatedBlockMachines {

    /** multi block structure state: 0:NO multi-structure, 1:mbs INACTIVE, 2:mbs ACTIVE */
   public static final BooleanProperty FORMED = BooleanProperty.create("formed");


    public AbstractMultiBlockBase(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isTransparent(BlockState state) {

        return state.hasProperty(FORMED) && state.get(FORMED);
    }

    @Override
    public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canBeReplacedByLogs(BlockState state, IWorldReader world, BlockPos pos) {
        return false;
    }

    //update multi-block structure state
    public static void updateMultiBlockState(boolean mbState, World world, BlockPos pos)
    {
        //check block exists
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof AbstractMultiBlockBase)
        {
            //set state
            world.setBlockState(pos, world.getBlockState(pos).with(FORMED, mbState), 2);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FORMED, false);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tile = worldIn.getTileEntity(pos);

        if(!worldIn.isRemote && tile instanceof MultiblockBaseTE)
        {
            MultiblockBaseTE tile2 = (MultiblockBaseTE) tile;

            if (tile2.hasMaster())
            {
                MultiblockHandler.resetStructure(worldIn, tile2.getMasterPos().getX(), tile2.getMasterPos().getY(), tile2.getMasterPos().getZ());
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        TileEntity tile = world.getTileEntity(pos);

        if(!world.isRemote && tile instanceof MultiblockBaseTE)
        {
            MultiblockBaseTE tile2 = (MultiblockBaseTE) tile;

            if (tile2.hasMaster())
            {
                MultiblockHandler.resetStructure(world, tile2.getMasterPos().getX(), tile2.getMasterPos().getY(), tile2.getMasterPos().getZ());
            }
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FORMED);
    }
}

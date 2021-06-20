package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockBaseTE;
import com.yor42.projectazure.libs.utils.MultiblockHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AbstractMultiBlockBase extends Block {

    /** multi block structure state: 0:NO multi-structure, 1:mbs INACTIVE, 2:mbs ACTIVE */
    public static final IntegerProperty MBS = IntegerProperty.create("mbs", 0, 2);


    public AbstractMultiBlockBase(Properties properties) {
        super(Properties.create(Material.IRON));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        if (state.get(MBS) > 0)
        {
            return BlockRenderType.INVISIBLE;
        }
        else
        {
            return BlockRenderType.MODEL;
        }
    }

    @Override
    public boolean isTransparent(BlockState state) {
        return state.get(MBS) > 0;
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
    public static void updateMultiBlockState(int mbState, World world, BlockPos pos)
    {
        //check block exists
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof AbstractMultiBlockBase)
        {
            //set state
            world.setBlockState(pos, world.getBlockState(pos).with(MBS, mbState), 2);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(MBS, 0);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tile = worldIn.getTileEntity(pos);

        if(!worldIn.isRemote && tile instanceof MultiblockBaseTE)
        {
            MultiblockBaseTE tile2 = (MultiblockBaseTE) tile;

            if (tile2.hasMaster())
            {
                MultiblockHandler.resetStructure(worldIn, tile2.getMasterPos().getX(), tile2.getMasterPos().getY(), tile2.getMasterPos().getZ());
            }
        }


        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
}

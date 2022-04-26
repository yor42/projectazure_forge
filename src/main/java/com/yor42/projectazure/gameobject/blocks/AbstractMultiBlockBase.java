package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockBaseTE;
import com.yor42.projectazure.libs.utils.MultiblockHandler;
import net.minecraft.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nullable;

public abstract class AbstractMultiBlockBase extends AbstractElectricMachineBlock {

    /** multi block structure state: 0:NO multi-structure, 1:mbs INACTIVE, 2:mbs ACTIVE */
   public static final BooleanProperty FORMED = BooleanProperty.create("formed");


    public AbstractMultiBlockBase(Properties properties) {
        super(properties);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {

        return state.hasProperty(FORMED) && state.getValue(FORMED);
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
    public static void updateMultiBlockState(boolean mbState, Level world, BlockPos pos)
    {
        //check block exists
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof AbstractMultiBlockBase)
        {
            //set state
            world.setBlock(pos, world.getBlockState(pos).setValue(FORMED, mbState), 2);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).setValue(FORMED, false);
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity tile = worldIn.getBlockEntity(pos);

        if(!worldIn.isClientSide && tile instanceof MultiblockBaseTE)
        {
            MultiblockBaseTE tile2 = (MultiblockBaseTE) tile;

            if (tile2.hasMaster())
            {
                MultiblockHandler.resetStructure(worldIn, tile2.getMasterPos().getX(), tile2.getMasterPos().getY(), tile2.getMasterPos().getZ());
            }
        }

        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        BlockEntity tile = world.getBlockEntity(pos);

        if(!world.isClientSide && tile instanceof MultiblockBaseTE)
        {
            MultiblockBaseTE tile2 = (MultiblockBaseTE) tile;

            if (tile2.hasMaster())
            {
                MultiblockHandler.resetStructure(world, tile2.getMasterPos().getX(), tile2.getMasterPos().getY(), tile2.getMasterPos().getZ());
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FORMED);
    }
}

package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityMetalPress;
import net.minecraft.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.PathNavigationRegion;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class AbstractMachineBlock extends AbstractContainerBlock{
    public AbstractMachineBlock(Properties properties) {
        super(properties);
    }

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public abstract BlockEntity createTileEntity(BlockState state, PathNavigationRegion world);

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, Level worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            this.interactWith(worldIn, pos, player);
            return ActionResultType.CONSUME;
        }
    }

    protected abstract void interactWith(Level worldIn, BlockPos pos, Player player);

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean shouldNotifyBlockChange() {
        return true;
    }

    @Override
    public boolean canDropInventory(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(ACTIVE, false);
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity te = worldIn.getBlockEntity(pos);
            if (te instanceof TileEntityMetalPress) {
                InventoryHelper.dropContents(worldIn, pos, (TileEntityMetalPress) te);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

}

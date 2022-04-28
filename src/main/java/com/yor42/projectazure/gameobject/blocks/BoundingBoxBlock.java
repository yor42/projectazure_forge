package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class BoundingBoxBlock extends AbstractContainerBlock{
    public BoundingBoxBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {

        BlockEntity tile = worldIn.getBlockEntity(pos);
        if(!worldIn.isClientSide && tile instanceof TileEntityBoundingBox) {
            TileEntityBoundingBox tile2 = (TileEntityBoundingBox) tile;

            if (tile2.hasMaster()) {
                NetworkHooks.openGui((ServerPlayer)player, (MenuProvider) worldIn.getBlockEntity(tile2.getMasterPos()));
            }
        }

        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        BlockEntity tile = worldIn.getBlockEntity(pos);

        if(!worldIn.isClientSide && tile instanceof TileEntityBoundingBox) {
            TileEntityBoundingBox tile2 = (TileEntityBoundingBox) tile;

            if (tile2.hasMaster()) {
                worldIn.destroyBlock(tile2.getMasterPos(), true, player);
            }
        }

        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        BlockEntity tile = world.getBlockEntity(pos);

        if(!world.isClientSide && tile instanceof TileEntityBoundingBox) {
            TileEntityBoundingBox tile2 = (TileEntityBoundingBox) tile;

            if (tile2.hasMaster()) {
                world.destroyBlock(tile2.getMasterPos(), true, null);
            }
        }

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return null;
    }
}

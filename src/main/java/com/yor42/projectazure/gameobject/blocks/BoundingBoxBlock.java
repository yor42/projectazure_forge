package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityBoundingBox;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.MenuProvider;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.LevelReader;
import net.minecraft.world.Level;
import net.minecraftforge.fml.network.NetworkHooks;

public class BoundingBoxBlock extends AbstractContainerBlock{
    public BoundingBoxBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public boolean shouldNotifyBlockChange() {
        return true;
    }

    @Override
    public boolean canBeReplacedByLeaves(BlockState state, LevelReader world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canBeReplacedByLogs(BlockState state, LevelReader world, BlockPos pos) {
        return false;
    }

    @Override
    public ActionResultType use(BlockState state, Level worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        BlockEntity tile = worldIn.getBlockEntity(pos);
        if(!worldIn.isClientSide && tile instanceof TileEntityBoundingBox) {
            TileEntityBoundingBox tile2 = (TileEntityBoundingBox) tile;

            if (tile2.hasMaster()) {
                NetworkHooks.openGui((ServerPlayerEntity)player, (MenuProvider) worldIn.getBlockEntity(tile2.getMasterPos()));
            }
        }

        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
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
}

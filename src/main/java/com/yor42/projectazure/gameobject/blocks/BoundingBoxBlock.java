package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityBoundingBox;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
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
    public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canBeReplacedByLogs(BlockState state, IWorldReader world, BlockPos pos) {
        return false;
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        TileEntity tile = worldIn.getBlockEntity(pos);
        if(!worldIn.isClientSide && tile instanceof TileEntityBoundingBox) {
            TileEntityBoundingBox tile2 = (TileEntityBoundingBox) tile;

            if (tile2.hasMaster()) {
                NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider) worldIn.getBlockEntity(tile2.getMasterPos()));
            }
        }

        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tile = worldIn.getBlockEntity(pos);

        if(!worldIn.isClientSide && tile instanceof TileEntityBoundingBox) {
            TileEntityBoundingBox tile2 = (TileEntityBoundingBox) tile;

            if (tile2.hasMaster()) {
                worldIn.destroyBlock(tile2.getMasterPos(), true, player);
            }
        }

        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        TileEntity tile = world.getBlockEntity(pos);

        if(!world.isClientSide && tile instanceof TileEntityBoundingBox) {
            TileEntityBoundingBox tile2 = (TileEntityBoundingBox) tile;

            if (tile2.hasMaster()) {
                world.destroyBlock(tile2.getMasterPos(), true, null);
            }
        }

    }
}

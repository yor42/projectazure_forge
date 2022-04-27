package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityBasicRefinery;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PathNavigationRegion;
import net.minecraft.world.Level;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BasicRefineryBlock extends AbstractMachineBlock {

    public BasicRefineryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, PathNavigationRegion world) {
        return new TileEntityBasicRefinery();
    }

    @Override
    protected void interactWith(Level worldIn, BlockPos pos, PlayerEntity player) {
        BlockEntity TileentityAtPos = worldIn.getBlockEntity(pos);
        if(TileentityAtPos instanceof TileEntityBasicRefinery && player instanceof ServerPlayerEntity && !worldIn.isClientSide()){
            TileEntityBasicRefinery TE = (TileEntityBasicRefinery) TileentityAtPos;
            NetworkHooks.openGui((ServerPlayerEntity) player, TE, TE::encodeExtraData);
        }
    }


    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity te = worldIn.getBlockEntity(pos);
            if (te instanceof TileEntityBasicRefinery) {
                InventoryHelper.dropContents(worldIn, pos, (TileEntityBasicRefinery) te);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }
}

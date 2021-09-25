package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityBasicRefinery;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
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
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityBasicRefinery();
    }

    @Override
    protected void interactWith(World worldIn, BlockPos pos, PlayerEntity player) {
        TileEntity TileentityAtPos = worldIn.getTileEntity(pos);
        if(TileentityAtPos instanceof TileEntityBasicRefinery && player instanceof ServerPlayerEntity && !worldIn.isRemote()){
            TileEntityBasicRefinery TE = (TileEntityBasicRefinery) TileentityAtPos;
            NetworkHooks.openGui((ServerPlayerEntity) player, TE, TE::encodeExtraData);
        }
    }


    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.isIn(newState.getBlock())) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityBasicRefinery) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityBasicRefinery) te);
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }
}

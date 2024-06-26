package com.yor42.projectazure.gameobject.blocks.machines;

import com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityBasicRefinery;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

public class BasicRefineryBlock extends AbstractMachineBlock {

    public BasicRefineryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockpos, BlockState blockstate) {
        return new TileEntityBasicRefinery(blockpos, blockstate);
    }

    @Override
    protected void interactWith(Level worldIn, BlockPos pos, Player player) {
        BlockEntity TileentityAtPos = worldIn.getBlockEntity(pos);
        if(TileentityAtPos instanceof TileEntityBasicRefinery && player instanceof ServerPlayer && !worldIn.isClientSide()){
            TileEntityBasicRefinery TE = (TileEntityBasicRefinery) TileentityAtPos;
            NetworkHooks.openGui((ServerPlayer) player, TE, TE::encodeExtraData);
        }
    }


    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity te = worldIn.getBlockEntity(pos);
            if (te instanceof TileEntityBasicRefinery) {
                Containers.dropContents(worldIn, pos, (TileEntityBasicRefinery) te);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }
}

package com.yor42.projectazure.gameobject.blocks.machines;

import com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityCrystalGrowthChamber;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class CrystalGrowthChamberBlock extends AbstractMachineBlock {
    public CrystalGrowthChamberBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new TileEntityCrystalGrowthChamber();
    }

    @Override
    protected void interactWith(Level worldIn, BlockPos pos, Player player) {
        BlockEntity TileentityAtPos = worldIn.getBlockEntity(pos);
        if(TileentityAtPos instanceof TileEntityCrystalGrowthChamber && player instanceof ServerPlayer && !worldIn.isClientSide()){
            TileEntityCrystalGrowthChamber TE = (TileEntityCrystalGrowthChamber) TileentityAtPos;
            NetworkHooks.openGui((ServerPlayer) player, TE, TE::encodeExtraData);
        }
    }
}

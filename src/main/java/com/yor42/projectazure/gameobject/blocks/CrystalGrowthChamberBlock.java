package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityCrystalGrowthChamber;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PathNavigationRegion;
import net.minecraft.world.Level;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class CrystalGrowthChamberBlock extends AbstractMachineBlock{
    public CrystalGrowthChamberBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, PathNavigationRegion world) {
        return new TileEntityCrystalGrowthChamber();
    }

    @Override
    protected void interactWith(Level worldIn, BlockPos pos, PlayerEntity player) {
        BlockEntity TileentityAtPos = worldIn.getBlockEntity(pos);
        if(TileentityAtPos instanceof TileEntityCrystalGrowthChamber && player instanceof ServerPlayerEntity && !worldIn.isClientSide()){
            TileEntityCrystalGrowthChamber TE = (TileEntityCrystalGrowthChamber) TileentityAtPos;
            NetworkHooks.openGui((ServerPlayerEntity) player, TE, TE::encodeExtraData);
        }
    }
}

package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityRecruitBeacon;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PathNavigationRegion;
import net.minecraft.world.Level;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class RecruitBeaconBlock extends AbstractElectricMachineBlock {

    public RecruitBeaconBlock() {
        super((AbstractBlock.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL).noOcclusion()));
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, PathNavigationRegion world) {
        return new TileEntityRecruitBeacon();
    }

    @Override
    protected void interactWith(Level worldIn, BlockPos pos, PlayerEntity player) {
        BlockEntity TileentityAtPos = worldIn.getBlockEntity(pos);
        if(TileentityAtPos instanceof TileEntityRecruitBeacon && player instanceof ServerPlayerEntity && !worldIn.isClientSide()){
            TileEntityRecruitBeacon TE = (TileEntityRecruitBeacon) TileentityAtPos;
            NetworkHooks.openGui((ServerPlayerEntity) player, TE, TE::encodeExtraData);
        }
    }
}

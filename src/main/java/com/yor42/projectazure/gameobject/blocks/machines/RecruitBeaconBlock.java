package com.yor42.projectazure.gameobject.blocks.machines;

import com.yor42.projectazure.gameobject.blocks.AbstractElectricMachineBlock;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityRecruitBeacon;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class RecruitBeaconBlock extends AbstractElectricMachineBlock {

    public RecruitBeaconBlock() {
        super((BlockBehaviour.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL).noOcclusion()));
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new TileEntityRecruitBeacon();
    }

    @Override
    protected void interactWith(Level worldIn, BlockPos pos, Player player) {
        BlockEntity TileentityAtPos = worldIn.getBlockEntity(pos);
        if(TileentityAtPos instanceof TileEntityRecruitBeacon && player instanceof ServerPlayer && !worldIn.isClientSide()){
            TileEntityRecruitBeacon TE = (TileEntityRecruitBeacon) TileentityAtPos;
            NetworkHooks.openGui((ServerPlayer) player, TE, TE::encodeExtraData);
        }
    }
}

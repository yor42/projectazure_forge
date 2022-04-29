package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityMetalPress;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class MetalPressBlock extends AbstractElectricMachineBlock {


    public MetalPressBlock() {
        super((BlockBehaviour.Properties.of(Material.METAL).strength(3, 10).sound(SoundType.METAL).noOcclusion()));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMetalPress(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> type) {
        return type == Main.BASIC_REFINERY_BLOCK_ENTITY.get() ? TileEntityMetalPress::tick : null;
    }

    protected void interactWith(Level worldIn, BlockPos pos, Player player) {
        BlockEntity TileentityAtPos = worldIn.getBlockEntity(pos);
        if(TileentityAtPos instanceof TileEntityMetalPress && player instanceof ServerPlayer && !worldIn.isClientSide()){
            TileEntityMetalPress TE = (TileEntityMetalPress) TileentityAtPos;
            NetworkHooks.openGui((ServerPlayer) player, TE, TE::encodeExtraData);
        }
    }
}

package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityMetalPress;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.Level;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class MetalPressBlock extends AbstractElectricMachineBlock {


    public MetalPressBlock() {
        super((AbstractBlock.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL).noOcclusion()));
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityMetalPress();
    }

    protected void interactWith(Level worldIn, BlockPos pos, PlayerEntity player) {
        BlockEntity TileentityAtPos = worldIn.getBlockEntity(pos);
        if(TileentityAtPos instanceof TileEntityMetalPress && player instanceof ServerPlayerEntity && !worldIn.isClientSide()){
            TileEntityMetalPress TE = (TileEntityMetalPress) TileentityAtPos;
            NetworkHooks.openGui((ServerPlayerEntity) player, TE, TE::encodeExtraData);
        }
    }
}

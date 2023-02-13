package com.yor42.projectazure.gameobject.blocks.machines;

import com.yor42.projectazure.gameobject.blocks.AbstractElectricMachineBlock;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityBasicChemicalReactor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BasicChemicalReactorBlock extends AbstractElectricMachineBlock {
    public BasicChemicalReactorBlock() {
        super((AbstractBlock.Properties.of(Material.METAL).strength(3, 10).harvestLevel(2).sound(SoundType.METAL).noOcclusion()));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityBasicChemicalReactor();
    }

    @Override
    protected void interactWith(World worldIn, BlockPos pos, PlayerEntity player) {
        TileEntity TileentityAtPos = worldIn.getBlockEntity(pos);
        if(player instanceof ServerPlayerEntity && TileentityAtPos instanceof TileEntityBasicChemicalReactor){
            TileEntityBasicChemicalReactor TE = (TileEntityBasicChemicalReactor) TileentityAtPos;
            NetworkHooks.openGui((ServerPlayerEntity) player, TE, TE::encodeExtraData);
        }
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }
}

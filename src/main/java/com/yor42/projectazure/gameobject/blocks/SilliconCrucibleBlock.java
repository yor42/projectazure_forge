package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityBoundingBox;
import com.yor42.projectazure.setup.register.registerBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;

public class SilliconCrucibleBlock extends AbstractBigMachineBlock {
    public SilliconCrucibleBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return null;
    }

    @Override
    protected void interactWith(World worldIn, BlockPos pos, PlayerEntity player) {

    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        worldIn.setBlockAndUpdate(pos.above(), registerBlocks.BOUNDING_BOX.get().defaultBlockState());
        if(worldIn.getBlockEntity(pos.above()) instanceof TileEntityBoundingBox) {
            ((TileEntityBoundingBox) worldIn.getBlockEntity(pos.above())).setMaster(worldIn.getBlockEntity(pos));
        }
    }

    @Override
    public void DestroyBoundingBox(World worldIn, BlockPos pos, BlockState state, @Nullable PlayerEntity player) {
        worldIn.destroyBlock(pos.above(), false, player);
    }
}

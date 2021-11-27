package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.AbstractBigMachineBlock;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityBoundingBox;
import com.yor42.projectazure.setup.register.registerBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        worldIn.setBlockState(pos.up(), registerBlocks.BOUNDING_BOX.get().getDefaultState());
        if(worldIn.getTileEntity(pos.up()) instanceof TileEntityBoundingBox) {
            ((TileEntityBoundingBox) worldIn.getTileEntity(pos.up())).setMaster(worldIn.getTileEntity(pos));
        }
    }

    @Override
    public void DestroyBoundingBox(World worldIn, BlockPos pos, BlockState state, @Nullable PlayerEntity player) {
        worldIn.destroyBlock(pos.up(), false, player);
    }
}

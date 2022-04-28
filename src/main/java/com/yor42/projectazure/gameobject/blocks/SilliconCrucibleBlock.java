package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityBoundingBox;
import com.yor42.projectazure.setup.register.registerBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class SilliconCrucibleBlock extends AbstractBigMachineBlock {
    public SilliconCrucibleBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return null;
    }

    @Override
    protected void interactWith(Level worldIn, BlockPos pos, Player player) {

    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        worldIn.setBlockAndUpdate(pos.above(), registerBlocks.BOUNDING_BOX.get().defaultBlockState());
        if(worldIn.getBlockEntity(pos.above()) instanceof TileEntityBoundingBox) {
            ((TileEntityBoundingBox) worldIn.getBlockEntity(pos.above())).setMaster(worldIn.getBlockEntity(pos));
        }
    }

    @Override
    public void DestroyBoundingBox(Level worldIn, BlockPos pos, BlockState state, @Nullable Player player) {
        worldIn.destroyBlock(pos.above(), false, player);
    }
}

package com.yor42.projectazure.gameobject.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class AbstractBigMachineBlock extends AbstractElectricMachineBlock{
    public AbstractBigMachineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, world, pos, explosion);
        this.DestroyBoundingBox(world, pos, state, null);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(worldIn, pos, state, player);
        this.DestroyBoundingBox(worldIn, pos, state, player);
    }

    public abstract void DestroyBoundingBox(World worldIn, BlockPos pos, BlockState state, @Nullable PlayerEntity player);

}
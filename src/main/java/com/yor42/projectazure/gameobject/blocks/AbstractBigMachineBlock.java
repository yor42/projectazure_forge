package com.yor42.projectazure.gameobject.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class AbstractBigMachineBlock extends AbstractElectricMachineBlock{
    public AbstractBigMachineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, world, pos, explosion);
        this.DestroyBoundingBox(world, pos, state, null);
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(worldIn, pos, state, player);
        this.DestroyBoundingBox(worldIn, pos, state, player);
    }

    public abstract void DestroyBoundingBox(Level worldIn, BlockPos pos, BlockState state, @Nullable Player player);

}

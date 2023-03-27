package com.yor42.projectazure.gameobject.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathFinder;

public class CompanionSwimPathNavigator extends WaterBoundPathNavigation {
    public CompanionSwimPathNavigator(Mob entitylivingIn, Level worldIn) {
        super(entitylivingIn, worldIn);
    }

    @Override
    protected PathFinder createPathFinder(int p_179679_1_) {
        this.nodeEvaluator = new CompanionSwimNodeProcessor();
        return new PathFinder(this.nodeEvaluator, p_179679_1_);
    }

    @Override
    public boolean isStableDestination(BlockPos p_188555_1_) {
        BlockPos blockpos = p_188555_1_.below();
        BlockState blockStatebelow = this.level.getBlockState(blockpos);
        BlockState blockState = this.level.getBlockState(p_188555_1_);
        return blockStatebelow.isSolidRender(this.level, blockpos)||blockState.is(Blocks.WATER);
    }
}

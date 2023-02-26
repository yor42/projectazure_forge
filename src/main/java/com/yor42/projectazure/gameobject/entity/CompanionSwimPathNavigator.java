package com.yor42.projectazure.gameobject.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.SwimNodeProcessor;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CompanionSwimPathNavigator extends SwimmerPathNavigator {
    public CompanionSwimPathNavigator(MobEntity entitylivingIn, World worldIn) {
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

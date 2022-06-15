package com.yor42.projectazure.gameobject.entity;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathType;
import net.minecraft.pathfinding.SwimNodeProcessor;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BoatNodeProcessor extends SwimNodeProcessor {

    public BoatNodeProcessor(boolean p_i48927_1_) {
        super(p_i48927_1_);
    }

    public PathNodeType getBlockPathType(IBlockReader p_186330_1_, int p_186330_2_, int p_186330_3_, int p_186330_4_) {
        BlockPos blockpos = new BlockPos(p_186330_2_, p_186330_3_, p_186330_4_);
        FluidState fluidstate = p_186330_1_.getFluidState(blockpos);
        BlockState blockstate = p_186330_1_.getBlockState(blockpos);
        if (fluidstate.isEmpty() && blockstate.isPathfindable(p_186330_1_, blockpos.below(), PathType.WATER) && blockstate.isAir()) {
            return PathNodeType.BREACH;
        } else {
            return fluidstate.is(FluidTags.WATER) && blockstate.isPathfindable(p_186330_1_, blockpos, PathType.WATER) ? PathNodeType.WATER : PathNodeType.BLOCKED;
        }
    }
}

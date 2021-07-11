package com.yor42.projectazure.gameobject.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.pathfinding.*;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class CompanionSwimNodeProcessor extends SwimNodeProcessor {
    public CompanionSwimNodeProcessor(boolean p_i48927_1_) {
        super(p_i48927_1_);
    }

    public int func_222859_a(PathPoint[] p_222859_1_, PathPoint p_222859_2_) {
        int i = 0;

        for(Direction direction : Direction.values()) {
            PathPoint pathpoint = this.getWaterNode(p_222859_2_.x + direction.getXOffset(), p_222859_2_.y + direction.getYOffset(), p_222859_2_.z + direction.getZOffset());
            if (pathpoint != null && !pathpoint.visited) {
                p_222859_1_[i++] = pathpoint;
            }
        }

        return i;
    }

    private PathPoint getWaterNode(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
        return this.openPoint(p_186328_1_, p_186328_2_, p_186328_3_);
    }
}

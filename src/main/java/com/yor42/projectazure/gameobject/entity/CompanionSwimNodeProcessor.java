package com.yor42.projectazure.gameobject.entity;

import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.SwimNodeProcessor;
import net.minecraft.util.Direction;

public class CompanionSwimNodeProcessor extends SwimNodeProcessor {
    public CompanionSwimNodeProcessor(boolean p_i48927_1_) {
        super(p_i48927_1_);
    }

    public int getNeighbors(PathPoint[] p_222859_1_, PathPoint p_222859_2_) {
        int i = 0;

        for(Direction direction : Direction.values()) {
            PathPoint pathpoint = this.getWaterNode(p_222859_2_.x + direction.getStepX(), p_222859_2_.y + direction.getStepY(), p_222859_2_.z + direction.getStepZ());
            if (pathpoint != null && !pathpoint.closed) {
                p_222859_1_[i++] = pathpoint;
            }
        }

        return i;
    }

    private PathPoint getWaterNode(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
        return this.getNode(p_186328_1_, p_186328_2_, p_186328_3_);
    }
}

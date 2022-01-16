package com.yor42.projectazure.gameobject.entity.pathfinding;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import java.util.function.Function;
/*
 * This class is distributed as part of the ImprovedMobs Mod.
 * Get the Source Code in github:
 * https://github.com/Flemmli97/ImprovedMobs
 *
 * I wish could write my own but I have 0 idea how AI works. sorry Flemmli97 ;C
 */
public class CompanionWalkerNodeProcessor extends WalkNodeProcessor {
    @Override
    public int getNeighbors(@Nonnull PathPoint[] points, @Nonnull PathPoint origin) {
        int supervalue = super.getNeighbors(points, origin);
        return createLadderPathPointFor(supervalue, points, origin, this::getNode, this.level, this.mob);
    }

    public static int createLadderPathPointFor(int PathPointID, PathPoint[] PathPoints, PathPoint origin, Function<BlockPos, PathPoint> PathPointGetter, IBlockReader getter, MobEntity mob) {
        BlockPos.Mutable pos = new BlockPos.Mutable(origin.x, origin.y + 1, origin.z);
        if (getter.getBlockState(pos).isLadder(mob.level, pos, mob) || getter.getBlockState(pos.below()).isLadder(mob.level, pos.below(), mob)) {
            PathPoint node = PathPointGetter.apply(pos);
            if (node != null && !node.closed) {
                node.costMalus = 0;
                node.type = PathNodeType.WALKABLE;
                if (PathPointID + 1 < PathPoints.length)
                    PathPoints[PathPointID++] = node;
            }
        }
        pos.set(pos.getX(), pos.getY() - 2, pos.getZ());
        if (getter.getBlockState(pos).isLadder(mob.level, pos, mob)) {
            PathPoint node = PathPointGetter.apply(pos);
            if (node != null && !node.closed) {
                node.costMalus = 0;
                node.type = PathNodeType.WALKABLE;
                if (PathPointID + 1 < PathPoints.length)
                    PathPoints[PathPointID++] = node;
            }
        }
        return PathPointID;
    }
}

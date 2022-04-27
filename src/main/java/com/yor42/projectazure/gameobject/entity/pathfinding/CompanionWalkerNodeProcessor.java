package com.yor42.projectazure.gameobject.entity.pathfinding;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import javax.annotation.Nonnull;
import java.util.function.Function;
/*
 * This class is distributed as part of the ImprovedMobs Mod.
 * Get the Source Code in github:
 * https://github.com/Flemmli97/ImprovedMobs
 *
 * I wish could write my own but I have 0 idea how AI works. sorry Flemmli97 ;C
 */
public class CompanionWalkerNodeProcessor extends WalkNodeEvaluator {
    @Override
    public int getNeighbors(@Nonnull Node[] points, @Nonnull Node origin) {
        int supervalue = super.getNeighbors(points, origin);
        return createLadderNodeFor(supervalue, points, origin, this::getNode, this.level, this.mob);
    }

    public static int createLadderNodeFor(int NodeID, Node[] Nodes, Node origin, Function<BlockPos, Node> NodeGetter, PathNavigationRegion getter, Mob mob) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(origin.x, origin.y + 1, origin.z);
        if (getter.getBlockState(pos).isLadder(mob.level, pos, mob) || getter.getBlockState(pos.below()).isLadder(mob.level, pos.below(), mob)) {
            Node node = NodeGetter.apply(pos);
            if (node != null && !node.closed) {
                node.costMalus = 0;
                node.type = BlockPathTypes.WALKABLE;
                if (NodeID + 1 < Nodes.length)
                    Nodes[NodeID++] = node;
            }
        }
        pos.set(pos.getX(), pos.getY() - 2, pos.getZ());
        if (getter.getBlockState(pos).isLadder(mob.level, pos, mob)) {
            Node node = NodeGetter.apply(pos);
            if (node != null && !node.closed) {
                node.costMalus = 0;
                node.type = BlockPathTypes.WALKABLE;
                if (NodeID + 1 < Nodes.length)
                    Nodes[NodeID++] = node;
            }
        }
        return NodeID;
    }
}

package com.yor42.projectazure.gameobject.entity.pathfinding;

import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nonnull;
import java.util.function.Function;
/*
 * Part of class is distributed as part of the ImprovedMobs Mod.
 * Get the Source Code in github:
 * https://github.com/Flemmli97/ImprovedMobs
 */
public class CompanionWalkerNodeProcessor extends WalkNodeEvaluator {
    @Override
    public int getNeighbors(@Nonnull Node[] points, @Nonnull Node origin) {
        int supervalue = super.getNeighbors(points, origin);
        return createLadderPathPointFor(supervalue, points, origin, this::getNode, this.level, this.mob);
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter world, int p_186330_2_, int p_186330_3_, int p_186330_4_) {
        BlockPathTypes superValue = super.getBlockPathType(world, p_186330_2_, p_186330_3_, p_186330_4_);
        if(superValue == BlockPathTypes.OPEN) {
            if (world.getBlockState(new BlockPos(p_186330_2_, p_186330_3_, p_186330_4_).below()).is(BlockTags.CLIMBABLE)) {
                superValue = BlockPathTypes.WALKABLE;

            }
            else if(world.getBlockState(new BlockPos(p_186330_2_, p_186330_3_, p_186330_4_)).getBlock() instanceof WoolCarpetBlock){
                superValue = BlockPathTypes.WALKABLE;
            }
        }
        return superValue;
    }

    public static int createLadderPathPointFor(int PathPointID, Node[] PathPoints, Node origin, Function<BlockPos, Node> PathPointGetter, BlockGetter getter, Mob mob) {
        BlockPos pos = origin.asBlockPos();
        //up
        if (getter.getBlockState(pos.above()).isLadder(mob.level, pos.above(), mob) || getter.getBlockState(pos).isLadder(mob.level, pos, mob)) {
            Node node = PathPointGetter.apply(pos.above());
            if (node != null && !node.closed) {
                node.costMalus = 0;
                node.type = BlockPathTypes.WALKABLE;
                if (PathPointID + 1 < PathPoints.length)
                    PathPoints[PathPointID++] = node;
            }
        }
        //down
        if (getter.getBlockState(pos.below()).isLadder(mob.level, pos.below(), mob)) {
            Node node = PathPointGetter.apply(pos.below());
            if (node != null && !node.closed) {
                node.costMalus = 0;
                node.type = BlockPathTypes.WALKABLE;
                if (PathPointID + 1 < PathPoints.length)
                    PathPoints[PathPointID++] = node;
            }
        }
        return PathPointID;
    }
}

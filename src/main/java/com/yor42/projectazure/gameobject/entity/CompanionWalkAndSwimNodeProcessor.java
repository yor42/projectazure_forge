package com.yor42.projectazure.gameobject.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.Region;

public class CompanionWalkAndSwimNodeProcessor extends WalkNodeProcessor {
    private float oldWalkableCost;
    private float oldWaterBorderCost;

    public void prepare(Region p_225578_1_, MobEntity p_225578_2_) {
        super.prepare(p_225578_1_, p_225578_2_);
        p_225578_2_.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        this.oldWalkableCost = p_225578_2_.getPathfindingMalus(PathNodeType.WALKABLE);
        p_225578_2_.setPathfindingMalus(PathNodeType.WALKABLE, 6.0F);
        this.oldWaterBorderCost = p_225578_2_.getPathfindingMalus(PathNodeType.WATER_BORDER);
        p_225578_2_.setPathfindingMalus(PathNodeType.WATER_BORDER, 4.0F);
    }
    public void done() {
        this.mob.setPathfindingMalus(PathNodeType.WALKABLE, this.oldWalkableCost);
        this.mob.setPathfindingMalus(PathNodeType.WATER_BORDER, this.oldWaterBorderCost);
        super.done();
    }

    public PathNodeType getBlockPathType(IBlockReader p_186330_1_, int p_186330_2_, int p_186330_3_, int p_186330_4_) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        PathNodeType pathnodetype = getBlockPathTypeRaw(p_186330_1_, blockpos$mutable.set(p_186330_2_, p_186330_3_, p_186330_4_));
        if (pathnodetype == PathNodeType.WATER) {
            for(Direction direction : Direction.values()) {
                PathNodeType pathnodetype2 = getBlockPathTypeRaw(p_186330_1_, blockpos$mutable.set(p_186330_2_, p_186330_3_, p_186330_4_).move(direction));
                if (pathnodetype2 == PathNodeType.BLOCKED) {
                    return PathNodeType.WATER_BORDER;
                }
            }

            return PathNodeType.WATER;
        } else {
            if (pathnodetype == PathNodeType.OPEN && p_186330_3_ >= 1) {
                BlockState blockstate = p_186330_1_.getBlockState(new BlockPos(p_186330_2_, p_186330_3_ - 1, p_186330_4_));
                PathNodeType pathnodetype1 = getBlockPathTypeRaw(p_186330_1_, blockpos$mutable.set(p_186330_2_, p_186330_3_ - 1, p_186330_4_));
                if (pathnodetype1 != PathNodeType.WALKABLE && pathnodetype1 != PathNodeType.OPEN && pathnodetype1 != PathNodeType.LAVA) {
                    pathnodetype = PathNodeType.WALKABLE;
                } else {
                    pathnodetype = PathNodeType.OPEN;
                }

                if (pathnodetype1 == PathNodeType.DAMAGE_FIRE || blockstate.is(Blocks.MAGMA_BLOCK) || blockstate.is(BlockTags.CAMPFIRES)) {
                    pathnodetype = PathNodeType.DAMAGE_FIRE;
                }

                if (pathnodetype1 == PathNodeType.DAMAGE_CACTUS) {
                    pathnodetype = PathNodeType.DAMAGE_CACTUS;
                }

                if (pathnodetype1 == PathNodeType.DAMAGE_OTHER) {
                    pathnodetype = PathNodeType.DAMAGE_OTHER;
                }
            }

            if (pathnodetype == PathNodeType.WALKABLE) {
                pathnodetype = checkNeighbourBlocks(p_186330_1_, blockpos$mutable.set(p_186330_2_, p_186330_3_, p_186330_4_), pathnodetype);
            }

            return pathnodetype;
        }
    }
}

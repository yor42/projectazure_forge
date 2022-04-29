package com.yor42.projectazure.gameobject.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class CompanionWalkAndSwimNodeProcessor extends WalkNodeEvaluator {
    private float oldWalkableCost;
    private float oldWaterBorderCost;

    public void prepare(PathNavigationRegion p_225578_1_, Mob p_225578_2_) {
        super.prepare(p_225578_1_, p_225578_2_);
        p_225578_2_.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.oldWalkableCost = p_225578_2_.getPathfindingMalus(BlockPathTypes.WALKABLE);
        p_225578_2_.setPathfindingMalus(BlockPathTypes.WALKABLE, 6.0F);
        this.oldWaterBorderCost = p_225578_2_.getPathfindingMalus(BlockPathTypes.WATER_BORDER);
        p_225578_2_.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 4.0F);
    }
    public void done() {
        this.mob.setPathfindingMalus(BlockPathTypes.WALKABLE, this.oldWalkableCost);
        this.mob.setPathfindingMalus(BlockPathTypes.WATER_BORDER, this.oldWaterBorderCost);
        super.done();
    }

    public BlockPathTypes getBlockPathType(PathNavigationRegion p_186330_1_, int p_186330_2_, int p_186330_3_, int p_186330_4_) {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        BlockPathTypes pathnodetype = getBlockPathTypeRaw(p_186330_1_, blockpos$mutable.set(p_186330_2_, p_186330_3_, p_186330_4_));
        if (pathnodetype == BlockPathTypes.WATER) {
            for(Direction direction : Direction.values()) {
                BlockPathTypes pathnodetype2 = getBlockPathTypeRaw(p_186330_1_, blockpos$mutable.set(p_186330_2_, p_186330_3_, p_186330_4_).move(direction));
                if (pathnodetype2 == BlockPathTypes.BLOCKED) {
                    return BlockPathTypes.WATER_BORDER;
                }
            }

            return BlockPathTypes.WATER;
        } else {
            if (pathnodetype == BlockPathTypes.OPEN && p_186330_3_ >= 1) {
                BlockState blockstate = p_186330_1_.getBlockState(new BlockPos(p_186330_2_, p_186330_3_ - 1, p_186330_4_));
                BlockPathTypes pathnodetype1 = getBlockPathTypeRaw(p_186330_1_, blockpos$mutable.set(p_186330_2_, p_186330_3_ - 1, p_186330_4_));
                if (pathnodetype1 != BlockPathTypes.WALKABLE && pathnodetype1 != BlockPathTypes.OPEN && pathnodetype1 != BlockPathTypes.LAVA) {
                    pathnodetype = BlockPathTypes.WALKABLE;
                } else {
                    pathnodetype = BlockPathTypes.OPEN;
                }

                if (pathnodetype1 == BlockPathTypes.DAMAGE_FIRE || blockstate.is(Blocks.MAGMA_BLOCK) || blockstate.is(BlockTags.CAMPFIRES)) {
                    pathnodetype = BlockPathTypes.DAMAGE_FIRE;
                }

                if (pathnodetype1 == BlockPathTypes.DAMAGE_CACTUS) {
                    pathnodetype = BlockPathTypes.DAMAGE_CACTUS;
                }

                if (pathnodetype1 == BlockPathTypes.DAMAGE_OTHER) {
                    pathnodetype = BlockPathTypes.DAMAGE_OTHER;
                }
            }

            if (pathnodetype == BlockPathTypes.WALKABLE) {
                pathnodetype = checkNeighbourBlocks(p_186330_1_, blockpos$mutable.set(p_186330_2_, p_186330_3_, p_186330_4_), pathnodetype);
            }

            return pathnodetype;
        }
    }
}

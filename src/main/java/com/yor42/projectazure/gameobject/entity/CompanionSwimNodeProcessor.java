package com.yor42.projectazure.gameobject.entity;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MobEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.pathfinding.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

import static net.minecraft.pathfinding.WalkNodeProcessor.checkNeighbourBlocks;

public class CompanionSwimNodeProcessor extends NodeProcessor {

    public PathPoint getStart() {
        return super.getNode(MathHelper.floor(this.mob.getBoundingBox().minX), MathHelper.floor(this.mob.getBoundingBox().minY + 0.5D), MathHelper.floor(this.mob.getBoundingBox().minZ));
    }

    public FlaggedPathPoint getGoal(double p_224768_1_, double p_224768_3_, double p_224768_5_) {
        return new FlaggedPathPoint(super.getNode(MathHelper.floor(p_224768_1_ - (double)(this.mob.getBbWidth() / 2.0F)), MathHelper.floor(p_224768_3_ + 0.5D), MathHelper.floor(p_224768_5_ - (double)(this.mob.getBbWidth() / 2.0F))));
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

    public PathNodeType getBlockPathType(IBlockReader p_186319_1_, int p_186319_2_, int p_186319_3_, int p_186319_4_, MobEntity p_186319_5_, int p_186319_6_, int p_186319_7_, int p_186319_8_, boolean p_186319_9_, boolean p_186319_10_) {
        return this.getBlockPathType(p_186319_1_, p_186319_2_, p_186319_3_, p_186319_4_);
    }

    public PathNodeType getBlockPathType(IBlockReader world, int x, int y, int z) {
        BlockPos.Mutable blockpos = new BlockPos.Mutable(x, y, z);
        FluidState fluidstate = world.getFluidState(blockpos);
        BlockState blockstate = world.getBlockState(blockpos);
        if (fluidstate.isEmpty() && blockstate.isPathfindable(world, blockpos, PathType.LAND)) {
            return getBlockPathTypeStatic(world, blockpos);
        } else {
            return fluidstate.is(FluidTags.WATER) && blockstate.isPathfindable(world, blockpos, PathType.WATER) ? PathNodeType.WATER : PathNodeType.BLOCKED;
        }
    }

    public static PathNodeType getBlockPathTypeStatic(IBlockReader p_237231_0_, BlockPos.Mutable p_237231_1_) {
        int i = p_237231_1_.getX();
        int j = p_237231_1_.getY();
        int k = p_237231_1_.getZ();
        PathNodeType pathnodetype = getBlockPathTypeRaw(p_237231_0_, p_237231_1_);
        if (pathnodetype == PathNodeType.OPEN && j >= 1) {
            PathNodeType pathnodetype1 = getBlockPathTypeRaw(p_237231_0_, p_237231_1_.set(i, j - 1, k));
            pathnodetype = pathnodetype1 != PathNodeType.WALKABLE && pathnodetype1 != PathNodeType.OPEN && pathnodetype1 != PathNodeType.WATER && pathnodetype1 != PathNodeType.LAVA ? PathNodeType.WALKABLE : PathNodeType.OPEN;
            if (pathnodetype1 == PathNodeType.DAMAGE_FIRE) {
                pathnodetype = PathNodeType.DAMAGE_FIRE;
            }

            if (pathnodetype1 == PathNodeType.DAMAGE_CACTUS) {
                pathnodetype = PathNodeType.DAMAGE_CACTUS;
            }

            if (pathnodetype1 == PathNodeType.DAMAGE_OTHER) {
                pathnodetype = PathNodeType.DAMAGE_OTHER;
            }

            if (pathnodetype1 == PathNodeType.STICKY_HONEY) {
                pathnodetype = PathNodeType.STICKY_HONEY;
            }
        }

        if (pathnodetype == PathNodeType.WALKABLE) {
            pathnodetype = checkNeighbourBlocks(p_237231_0_, p_237231_1_.set(i, j, k), pathnodetype);
        }

        return pathnodetype;
    }

    protected static PathNodeType getBlockPathTypeRaw(IBlockReader p_237238_0_, BlockPos p_237238_1_) {
        BlockState blockstate = p_237238_0_.getBlockState(p_237238_1_);
        PathNodeType type = blockstate.getAiPathNodeType(p_237238_0_, p_237238_1_);
        if (type != null) return type;
        Block block = blockstate.getBlock();
        Material material = blockstate.getMaterial();
        if (blockstate.isAir(p_237238_0_, p_237238_1_)) {
            return PathNodeType.OPEN;
        } else if (!blockstate.is(BlockTags.TRAPDOORS) && !blockstate.is(Blocks.LILY_PAD)) {
            if (blockstate.is(Blocks.CACTUS)) {
                return PathNodeType.DAMAGE_CACTUS;
            } else if (blockstate.is(Blocks.SWEET_BERRY_BUSH)) {
                return PathNodeType.DAMAGE_OTHER;
            } else if (blockstate.is(Blocks.HONEY_BLOCK)) {
                return PathNodeType.STICKY_HONEY;
            } else if (blockstate.is(Blocks.COCOA)) {
                return PathNodeType.COCOA;
            } else {
                FluidState fluidstate = p_237238_0_.getFluidState(p_237238_1_);
                if (fluidstate.is(FluidTags.WATER)) {
                    return PathNodeType.WATER;
                } else if (fluidstate.is(FluidTags.LAVA)) {
                    return PathNodeType.LAVA;
                } else if (isBurningBlock(blockstate)) {
                    return PathNodeType.DAMAGE_FIRE;
                } else if (DoorBlock.isWoodenDoor(blockstate) && !blockstate.getValue(DoorBlock.OPEN)) {
                    return PathNodeType.DOOR_WOOD_CLOSED;
                } else if (block instanceof DoorBlock && material == Material.METAL && !blockstate.getValue(DoorBlock.OPEN)) {
                    return PathNodeType.DOOR_IRON_CLOSED;
                } else if (block instanceof DoorBlock && blockstate.getValue(DoorBlock.OPEN)) {
                    return PathNodeType.DOOR_OPEN;
                } else if (block instanceof AbstractRailBlock) {
                    return PathNodeType.RAIL;
                } else if (block instanceof LeavesBlock) {
                    return PathNodeType.LEAVES;
                } else if (!block.is(BlockTags.FENCES) && !block.is(BlockTags.WALLS) && (!(block instanceof FenceGateBlock) || blockstate.getValue(FenceGateBlock.OPEN))) {
                    return !blockstate.isPathfindable(p_237238_0_, p_237238_1_, PathType.LAND) ? PathNodeType.BLOCKED : PathNodeType.OPEN;
                } else {
                    return PathNodeType.FENCE;
                }
            }
        } else {
            return PathNodeType.TRAPDOOR;
        }
    }

    private static boolean isBurningBlock(BlockState p_237233_0_) {
        return p_237233_0_.is(BlockTags.FIRE) || p_237233_0_.is(Blocks.LAVA) || p_237233_0_.is(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(p_237233_0_);
    }

    @Nullable
    private PathPoint getWaterNode(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
        PathNodeType pathnodetype = this.isFree(p_186328_1_, p_186328_2_, p_186328_3_);
        return pathnodetype != PathNodeType.WATER ? null : this.getNode(p_186328_1_, p_186328_2_, p_186328_3_);
    }

    @Nullable
    protected PathPoint getNode(int p_176159_1_, int p_176159_2_, int p_176159_3_) {
        PathPoint pathpoint = null;
        PathNodeType pathnodetype = this.getBlockPathType(this.mob.level, p_176159_1_, p_176159_2_, p_176159_3_);
        float f = this.mob.getPathfindingMalus(pathnodetype);
        if (f >= 0.0F) {
            pathpoint = super.getNode(p_176159_1_, p_176159_2_, p_176159_3_);
            pathpoint.type = pathnodetype;
            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
            if (this.level.getFluidState(new BlockPos(p_176159_1_, p_176159_2_, p_176159_3_)).isEmpty()) {
                pathpoint.costMalus += 8.0F;
            }
        }

        return pathpoint;
    }

    private PathNodeType isFree(int p_186327_1_, int p_186327_2_, int p_186327_3_) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(int i = p_186327_1_; i < p_186327_1_ + this.entityWidth; ++i) {
            for(int j = p_186327_2_; j < p_186327_2_ + this.entityHeight; ++j) {
                for(int k = p_186327_3_; k < p_186327_3_ + this.entityDepth; ++k) {
                    FluidState fluidstate = this.level.getFluidState(blockpos$mutable.set(i, j, k));
                    BlockState blockstate = this.level.getBlockState(blockpos$mutable.set(i, j, k));
                    if (fluidstate.isEmpty() && blockstate.isPathfindable(this.level, blockpos$mutable.below(), PathType.WATER) && blockstate.isAir()) {
                        return PathNodeType.BREACH;
                    }

                    if (!fluidstate.is(FluidTags.WATER)) {
                        return PathNodeType.BLOCKED;
                    }
                }
            }
        }

        BlockState blockstate1 = this.level.getBlockState(blockpos$mutable);
        return blockstate1.isPathfindable(this.level, blockpos$mutable, PathType.WATER) ? PathNodeType.WATER : PathNodeType.BLOCKED;
    }
}

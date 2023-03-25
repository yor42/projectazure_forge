package com.yor42.projectazure.gameobject.entity;

import net.minecraft.block.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.pathfinding.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nullable;

import static net.minecraft.pathfinding.WalkNodeProcessor.checkNeighbourBlocks;

public clasimport net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.Target;

net.minecraft.world.level.pathfinder.WalkNodeEvaluatoraluator {

    public Node getStart() {
        return super.getNode(Mth.floor(this.mob.getBoundingBox().minX), Mth.floor(this.mob.getBoundingBox().minY + 0.5D), Mth.floor(this.mob.getBoundingBox().minZ));
    }

    public Target getGoal(double p_224768_1_, double p_224768_3_, double p_224768_5_) {
        return new Target(super.getNode(Mth.floor(p_224768_1_ - (double)(this.mob.getBbWidth() / 2.0F)), Mth.floor(p_224768_3_ + 0.5D), Mth.floor(p_224768_5_ - (double)(this.mob.getBbWidth() / 2.0F))));
    }

    public int getNeighbors(Node[] p_222859_1_, Node p_222859_2_) {
        int i = 0;

        for(Direction direction : Direction.values()) {
            Node pathpoint = this.getWaterNode(p_222859_2_.x + direction.getStepX(), p_222859_2_.y + direction.getStepY(), p_222859_2_.z + direction.getStepZ());
            if (pathpoint != null && !pathpoint.closed) {
                p_222859_1_[i++] = pathpoint;
            }
        }

        return i;
    }

    public BlockPathTypes getBlockPathType(BlockGetter p_186319_1_, int p_186319_2_, int p_186319_3_, int p_186319_4_, Mob p_186319_5_, int p_186319_6_, int p_186319_7_, int p_186319_8_, boolean p_186319_9_, boolean p_186319_10_) {
        return this.getBlockPathType(p_186319_1_, p_186319_2_, p_186319_3_, p_186319_4_);
    }

    public BlockPathTypes getBlockPathType(BlockGetter world, int x, int y, int z) {
        BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos(x, y, z);
        FluidState fluidstate = world.getFluidState(blockpos);
        BlockState blockstate = world.getBlockState(blockpos);
        if (fluidstate.isEmpty() && blockstate.isPathfindable(world, blockpos, PathComputationType.LAND)) {
            return getBlockPathTypeStatic(world, blockpos);
        } else {
            return fluidstate.is(FluidTags.WATER) && blockstate.isPathfindable(world, blockpos, PathComputationType.WATER) ? BlockPathTypes.WATER : BlockPathTypes.BLOCKED;
        }
    }

    public static BlockPathTypes getBlockPathTypeStatic(BlockGetter p_237231_0_, BlockPos.MutableBlockPos p_237231_1_) {
        int i = p_237231_1_.getX();
        int j = p_237231_1_.getY();
        int k = p_237231_1_.getZ();
        BlockPathTypes pathnodetype = getBlockPathTypeRaw(p_237231_0_, p_237231_1_);
        if (pathnodetype == BlockPathTypes.OPEN && j >= 1) {
            BlockPathTypes pathnodetype1 = getBlockPathTypeRaw(p_237231_0_, p_237231_1_.set(i, j - 1, k));
            pathnodetype = pathnodetype1 != BlockPathTypes.WALKABLE && pathnodetype1 != BlockPathTypes.OPEN && pathnodetype1 != BlockPathTypes.WATER && pathnodetype1 != BlockPathTypes.LAVA ? BlockPathTypes.WALKABLE : BlockPathTypes.OPEN;
            if (pathnodetype1 == BlockPathTypes.DAMAGE_FIRE) {
                pathnodetype = BlockPathTypes.DAMAGE_FIRE;
            }

            if (pathnodetype1 == BlockPathTypes.DAMAGE_CACTUS) {
                pathnodetype = BlockPathTypes.DAMAGE_CACTUS;
            }

            if (pathnodetype1 == BlockPathTypes.DAMAGE_OTHER) {
                pathnodetype = BlockPathTypes.DAMAGE_OTHER;
            }

            if (pathnodetype1 == BlockPathTypes.STICKY_HONEY) {
                pathnodetype = BlockPathTypes.STICKY_HONEY;
            }
        }

        if (pathnodetype == BlockPathTypes.WALKABLE) {
            pathnodetype = checkNeighbourBlocks(p_237231_0_, p_237231_1_.set(i, j, k), pathnodetype);
        }

        return pathnodetype;
    }

    protected static BlockPathTypes getBlockPathTypeRaw(BlockGetter p_237238_0_, BlockPos p_237238_1_) {
        BlockState blockstate = p_237238_0_.getBlockState(p_237238_1_);
        BlockPathTypes type = blockstate.getAiPathNodeType(p_237238_0_, p_237238_1_);
        if (type != null) return type;
        Block block = blockstate.getBlock();
        Material material = blockstate.getMaterial();
        if (blockstate.isAir(p_237238_0_, p_237238_1_)) {
            return BlockPathTypes.OPEN;
        } else if (!blockstate.is(BlockTags.TRAPDOORS) && !blockstate.is(Blocks.LILY_PAD)) {
            if (blockstate.is(Blocks.CACTUS)) {
                return BlockPathTypes.DAMAGE_CACTUS;
            } else if (blockstate.is(Blocks.SWEET_BERRY_BUSH)) {
                return BlockPathTypes.DAMAGE_OTHER;
            } else if (blockstate.is(Blocks.HONEY_BLOCK)) {
                return BlockPathTypes.STICKY_HONEY;
            } else if (blockstate.is(Blocks.COCOA)) {
                return BlockPathTypes.COCOA;
            } else {
                FluidState fluidstate = p_237238_0_.getFluidState(p_237238_1_);
                if (fluidstate.is(FluidTags.WATER)) {
                    return BlockPathTypes.WATER;
                } else if (fluidstate.is(FluidTags.LAVA)) {
                    return BlockPathTypes.LAVA;
                } else if (isBurningBlock(blockstate)) {
                    return BlockPathTypes.DAMAGE_FIRE;
                } else if (DoorBlock.isWoodenDoor(blockstate) && !blockstate.getValue(DoorBlock.OPEN)) {
                    return BlockPathTypes.DOOR_WOOD_CLOSED;
                } else if (block instanceof DoorBlock && material == Material.METAL && !blockstate.getValue(DoorBlock.OPEN)) {
                    return BlockPathTypes.DOOR_IRON_CLOSED;
                } else if (block instanceof DoorBlock && blockstate.getValue(DoorBlock.OPEN)) {
                    return BlockPathTypes.DOOR_OPEN;
                } else if (block instanceof BaseRailBlock) {
                    return BlockPathTypes.RAIL;
                } else if (block instanceof LeavesBlock) {
                    return BlockPathTypes.LEAVES;
                } else if (!block.is(BlockTags.FENCES) && !block.is(BlockTags.WALLS) && (!(block instanceof FenceGateBlock) || blockstate.getValue(FenceGateBlock.OPEN))) {
                    return !blockstate.isPathfindable(p_237238_0_, p_237238_1_, PathComputationType.LAND) ? BlockPathTypes.BLOCKED : BlockPathTypes.OPEN;
                } else {
                    return BlockPathTypes.FENCE;
                }
            }
        } else {
            return BlockPathTypes.TRAPDOOR;
        }
    }

    private static boolean isBurningBlock(BlockState p_237233_0_) {
        return p_237233_0_.is(BlockTags.FIRE) || p_237233_0_.is(Blocks.LAVA) || p_237233_0_.is(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(p_237233_0_);
    }

    @Nullable
    private Node getWaterNode(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
        BlockPathTypes pathnodetype = this.isFree(p_186328_1_, p_186328_2_, p_186328_3_);
        return pathnodetype != BlockPathTypes.WATER ? null : this.getNode(p_186328_1_, p_186328_2_, p_186328_3_);
    }

    @Nullable
    protected Node getNode(int p_176159_1_, int p_176159_2_, int p_176159_3_) {
        Node pathpoint = null;
        BlockPathTypes pathnodetype = this.getBlockPathType(this.mob.level, p_176159_1_, p_176159_2_, p_176159_3_);
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

    private BlockPathTypes isFree(int p_186327_1_, int p_186327_2_, int p_186327_3_) {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();

        for(int i = p_186327_1_; i < p_186327_1_ + this.entityWidth; ++i) {
            for(int j = p_186327_2_; j < p_186327_2_ + this.entityHeight; ++j) {
                for(int k = p_186327_3_; k < p_186327_3_ + this.entityDepth; ++k) {
                    FluidState fluidstate = this.level.getFluidState(blockpos$mutable.set(i, j, k));
                    BlockState blockstate = this.level.getBlockState(blockpos$mutable.set(i, j, k));
                    if (fluidstate.isEmpty() && blockstate.isPathfindable(this.level, blockpos$mutable.below(), PathComputationType.WATER) && blockstate.isAir()) {
                        return BlockPathTypes.BREACH;
                    }

                    if (!fluidstate.is(FluidTags.WATER)) {
                        return BlockPathTypes.BLOCKED;
                    }
                }
            }
        }

        BlockState blockstate1 = this.level.getBlockState(blockpos$mutable);
        return blockstate1.isPathfindable(this.level, blockpos$mutable, PathComputationType.WATER) ? BlockPathTypes.WATER : BlockPathTypes.BLOCKED;
    }
}

package com.yor42.projectazure.gameobject.entity;

import com.google.common.collect.ImmutableSet;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Iterator;

public class CompanionGroundPathNavigator extends GroundPathNavigator {

    protected boolean shouldAvoidSun;
    private final AbstractEntityCompanion companion;

    public CompanionGroundPathNavigator(AbstractEntityCompanion entity, World world) {
        super(entity, world);
        this.companion = entity;
    }

    protected PathFinder createPathFinder(int p_179679_1_) {
        this.nodeEvaluator = new WalkAndSwimNodeProcessor();
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, p_179679_1_);
    }

    protected boolean canUpdatePath() {
        return this.mob.isOnGround() || this.isInLiquid() || this.mob.isPassenger();
    }

    protected Vector3d getTempMobPos() {
        return new Vector3d(this.mob.getX(), (double)this.getPathablePosY(), this.mob.getZ());
    }

    public Path createPath(BlockPos blockposIn, int distance) {
        BlockPos BlockPosDown;
        if (this.level.getBlockState(blockposIn).isAir()) {
            BlockPosDown = blockposIn.below();
            while (BlockPosDown.getY() > 0 && this.level.getBlockState(BlockPosDown).isAir()) {
                BlockPosDown = BlockPosDown.below();
            }

            if (BlockPosDown.getY() > 0) {
                return this.createPath(ImmutableSet.of(BlockPosDown.above()), 8, false, distance);
            }

            while(BlockPosDown.getY() < this.level.getMaxBuildHeight() && this.level.getBlockState(BlockPosDown).isAir()) {
                BlockPosDown = BlockPosDown.above();
            }

            blockposIn = BlockPosDown;
        }

        if (!this.level.getBlockState(blockposIn).getMaterial().isSolid()) {
            return super.createPath(blockposIn, distance);
        } else {
            BlockPosDown = blockposIn.above();
            while (BlockPosDown.getY() < this.level.getMaxBuildHeight() && this.level.getBlockState(BlockPosDown).getMaterial().isSolid()) {
                BlockPosDown = BlockPosDown.above();
            }

            return super.createPath(BlockPosDown, distance);
        }
    }

    public Path createPath(Entity p_75494_1_, int p_75494_2_) {
        return this.createPath(p_75494_1_.blockPosition(), p_75494_2_);
    }

    private int getPathablePosY() {
        if (this.mob.isInWater() && this.canFloat()) {
            int lvt_1_1_ = MathHelper.floor(this.mob.getY());
            Block lvt_2_1_ = this.level.getBlockState(new BlockPos(this.mob.getX(), (double)lvt_1_1_, this.mob.getZ())).getBlock();
            int lvt_3_1_ = 0;

            do {
                if (lvt_2_1_ != Blocks.WATER) {
                    return lvt_1_1_;
                }

                ++lvt_1_1_;
                lvt_2_1_ = this.level.getBlockState(new BlockPos(this.mob.getX(), (double)lvt_1_1_, this.mob.getZ())).getBlock();
                ++lvt_3_1_;
            } while(lvt_3_1_ <= 16);

            return MathHelper.floor(this.mob.getY());
        } else {
            if(this.companion.isSailing()){
                int lvt_1_1_ = MathHelper.floor(this.mob.getY());
                Block lvt_2_1_ = this.level.getBlockState(new BlockPos(this.mob.getX(), (double)lvt_1_1_, this.mob.getZ())).getBlock();
                int lvt_3_1_ = 0;

                do {
                    if (lvt_2_1_ != Blocks.WATER) {
                        return lvt_1_1_-1;
                    }

                    ++lvt_1_1_;
                    lvt_2_1_ = this.level.getBlockState(new BlockPos(this.mob.getX(), (double)lvt_1_1_, this.mob.getZ())).getBlock();
                    ++lvt_3_1_;
                } while(lvt_3_1_ <= 16);

                return MathHelper.floor(this.mob.getY());
            }
            return MathHelper.floor(this.mob.getY() + 0.5D);
        }
    }

    protected void trimPath() {
        super.trimPath();
        if (this.shouldAvoidSun) {
            if (this.level.canSeeSky(new BlockPos(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()))) {
                return;
            }

            for(int lvt_1_1_ = 0; lvt_1_1_ < this.path.getNodeCount(); ++lvt_1_1_) {
                PathPoint lvt_2_1_ = this.path.getNode(lvt_1_1_);
                if (this.level.canSeeSky(new BlockPos(lvt_2_1_.x, lvt_2_1_.y, lvt_2_1_.z))) {
                    this.path.truncateNodes(lvt_1_1_);
                    return;
                }
            }
        }

    }

    protected boolean canMoveDirectly(Vector3d p_75493_1_, Vector3d p_75493_2_, int p_75493_3_, int p_75493_4_, int p_75493_5_) {
        int lvt_6_1_ = MathHelper.floor(p_75493_1_.x);
        int lvt_7_1_ = MathHelper.floor(p_75493_1_.z);
        double lvt_8_1_ = p_75493_2_.x - p_75493_1_.x;
        double lvt_10_1_ = p_75493_2_.z - p_75493_1_.z;
        double lvt_12_1_ = lvt_8_1_ * lvt_8_1_ + lvt_10_1_ * lvt_10_1_;
        if (lvt_12_1_ < 1.0E-8D) {
            return false;
        } else {
            double lvt_14_1_ = 1.0D / Math.sqrt(lvt_12_1_);
            lvt_8_1_ *= lvt_14_1_;
            lvt_10_1_ *= lvt_14_1_;
            p_75493_3_ += 2;
            p_75493_5_ += 2;
            if (!this.isSafeToStandAt(lvt_6_1_, MathHelper.floor(p_75493_1_.y), lvt_7_1_, p_75493_3_, p_75493_4_, p_75493_5_, p_75493_1_, lvt_8_1_, lvt_10_1_)) {
                return false;
            } else {
                p_75493_3_ -= 2;
                p_75493_5_ -= 2;
                double lvt_16_1_ = 1.0D / Math.abs(lvt_8_1_);
                double lvt_18_1_ = 1.0D / Math.abs(lvt_10_1_);
                double lvt_20_1_ = (double)lvt_6_1_ - p_75493_1_.x;
                double lvt_22_1_ = (double)lvt_7_1_ - p_75493_1_.z;
                if (lvt_8_1_ >= 0.0D) {
                    ++lvt_20_1_;
                }

                if (lvt_10_1_ >= 0.0D) {
                    ++lvt_22_1_;
                }

                lvt_20_1_ /= lvt_8_1_;
                lvt_22_1_ /= lvt_10_1_;
                int lvt_24_1_ = lvt_8_1_ < 0.0D ? -1 : 1;
                int lvt_25_1_ = lvt_10_1_ < 0.0D ? -1 : 1;
                int lvt_26_1_ = MathHelper.floor(p_75493_2_.x);
                int lvt_27_1_ = MathHelper.floor(p_75493_2_.z);
                int lvt_28_1_ = lvt_26_1_ - lvt_6_1_;
                int lvt_29_1_ = lvt_27_1_ - lvt_7_1_;

                do {
                    if (lvt_28_1_ * lvt_24_1_ <= 0 && lvt_29_1_ * lvt_25_1_ <= 0) {
                        return true;
                    }

                    if (lvt_20_1_ < lvt_22_1_) {
                        lvt_20_1_ += lvt_16_1_;
                        lvt_6_1_ += lvt_24_1_;
                        lvt_28_1_ = lvt_26_1_ - lvt_6_1_;
                    } else {
                        lvt_22_1_ += lvt_18_1_;
                        lvt_7_1_ += lvt_25_1_;
                        lvt_29_1_ = lvt_27_1_ - lvt_7_1_;
                    }
                } while(this.isSafeToStandAt(lvt_6_1_, MathHelper.floor(p_75493_1_.y), lvt_7_1_, p_75493_3_, p_75493_4_, p_75493_5_, p_75493_1_, lvt_8_1_, lvt_10_1_));

                return false;
            }
        }
    }

    private boolean isSafeToStandAt(int p_179683_1_, int p_179683_2_, int p_179683_3_, int p_179683_4_, int p_179683_5_, int p_179683_6_, Vector3d p_179683_7_, double p_179683_8_, double p_179683_10_) {
        int lvt_12_1_ = p_179683_1_ - p_179683_4_ / 2;
        int lvt_13_1_ = p_179683_3_ - p_179683_6_ / 2;
        if (!this.isPositionClear(lvt_12_1_, p_179683_2_, lvt_13_1_, p_179683_4_, p_179683_5_, p_179683_6_, p_179683_7_, p_179683_8_, p_179683_10_)) {
            return false;
        } else {
            for(int lvt_14_1_ = lvt_12_1_; lvt_14_1_ < lvt_12_1_ + p_179683_4_; ++lvt_14_1_) {
                for(int lvt_15_1_ = lvt_13_1_; lvt_15_1_ < lvt_13_1_ + p_179683_6_; ++lvt_15_1_) {
                    double lvt_16_1_ = (double)lvt_14_1_ + 0.5D - p_179683_7_.x;
                    double lvt_18_1_ = (double)lvt_15_1_ + 0.5D - p_179683_7_.z;
                    if (!(lvt_16_1_ * p_179683_8_ + lvt_18_1_ * p_179683_10_ < 0.0D)) {
                        PathNodeType lvt_20_1_ = this.nodeEvaluator.getBlockPathType(this.level, lvt_14_1_, p_179683_2_ - 1, lvt_15_1_, this.mob, p_179683_4_, p_179683_5_, p_179683_6_, true, true);
                        if (!this.hasValidPathType(lvt_20_1_)) {
                            return false;
                        }

                        lvt_20_1_ = this.nodeEvaluator.getBlockPathType(this.level, lvt_14_1_, p_179683_2_, lvt_15_1_, this.mob, p_179683_4_, p_179683_5_, p_179683_6_, true, true);
                        float lvt_21_1_ = this.mob.getPathfindingMalus(lvt_20_1_);
                        if (lvt_21_1_ < 0.0F || lvt_21_1_ >= 8.0F) {
                            return false;
                        }

                        if (lvt_20_1_ == PathNodeType.DAMAGE_FIRE || lvt_20_1_ == PathNodeType.DANGER_FIRE || lvt_20_1_ == PathNodeType.DAMAGE_OTHER) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    protected boolean hasValidPathType(PathNodeType p_230287_1_) {
        if (p_230287_1_ == PathNodeType.WATER) {
            return false;
        } else if (p_230287_1_ == PathNodeType.LAVA) {
            return false;
        } else {
            return p_230287_1_ != PathNodeType.OPEN;
        }
    }

    private boolean isPositionClear(int p_179692_1_, int p_179692_2_, int p_179692_3_, int p_179692_4_, int p_179692_5_, int p_179692_6_, Vector3d p_179692_7_, double p_179692_8_, double p_179692_10_) {
        Iterator var12 = BlockPos.betweenClosed(new BlockPos(p_179692_1_, p_179692_2_, p_179692_3_), new BlockPos(p_179692_1_ + p_179692_4_ - 1, p_179692_2_ + p_179692_5_ - 1, p_179692_3_ + p_179692_6_ - 1)).iterator();

        BlockPos lvt_13_1_;
        double lvt_14_1_;
        double lvt_16_1_;
        do {
            if (!var12.hasNext()) {
                return true;
            }

            lvt_13_1_ = (BlockPos)var12.next();
            lvt_14_1_ = (double)lvt_13_1_.getX() + 0.5D - p_179692_7_.x;
            lvt_16_1_ = (double)lvt_13_1_.getZ() + 0.5D - p_179692_7_.z;
        } while(lvt_14_1_ * p_179692_8_ + lvt_16_1_ * p_179692_10_ < 0.0D || this.level.getBlockState(lvt_13_1_).isPathfindable(this.level, lvt_13_1_, PathType.LAND));

        return false;
    }

    public void setCanOpenDoors(boolean p_179688_1_) {
        this.nodeEvaluator.setCanOpenDoors(p_179688_1_);
    }

    public boolean canOpenDoors() {
        return this.nodeEvaluator.canPassDoors();
    }

    public void setAvoidSun(boolean p_179685_1_) {
        this.shouldAvoidSun = p_179685_1_;
    }
}

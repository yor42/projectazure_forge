package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.pathfinding.CompanionWalkerNodeProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class CompanionGroundPathNavigator extends GroundPathNavigation {

    protected boolean shouldAvoidSun;
    private final AbstractEntityCompanion companion;

    public CompanionGroundPathNavigator(AbstractEntityCompanion entity, Level world) {
        super(entity, world);
        this.companion = entity;
    }

    @Override
    public void tick() {
        ++this.tick;
        if (this.hasDelayedRecomputation) {
            this.recomputePath();
        }

        if (!this.isDone()) {
            Path path = this.path;
            if (this.canUpdatePath()) {
                this.followThePath();
            } else if (path != null && !path.isDone()) {
                Vec3 vector3d = this.getTempMobPos();
                Vec3 vector3d1 = path.getNextEntityPos(this.mob);
                if (((this.mob.onClimbable() && Math.abs(vector3d.y - vector3d1.y)<0.3) || vector3d.y > vector3d1.y) && !this.mob.isOnGround() && Mth.floor(vector3d.x) == Mth.floor(vector3d1.x) && Mth.floor(vector3d.z) == Mth.floor(vector3d1.z)) {
                    path.advance();
                }
            }

            DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
            if (!this.isDone()) {
                Vec3 vector3d2 = this.path.getNextEntityPos(this.mob);
                BlockPos blockpos = new BlockPos(vector3d2);
                double y = this.level.getBlockState(blockpos.below()).getMaterial() == Material.AIR ? vector3d2.y : WalkNodeEvaluator.getFloorLevel(this.level, blockpos);
                this.mob.getMoveControl().setWantedPosition(vector3d2.x, y, vector3d2.z, this.speedModifier);
            }
        }
    }

    protected void followThePath() {
        Vec3 tempMobPosition = this.getTempMobPos();
        this.maxDistanceToWaypoint = this.mob.getBbWidth() > 0.75F ? this.mob.getBbWidth() / 2.0F : 0.75F - this.mob.getBbWidth() / 2.0F;
        Vec3i vector3i = this.path.getNextNodePos();
        double d0 = Math.abs(this.mob.getX() - ((double)vector3i.getX() + (this.mob.getBbWidth() + 1) / 2D)); //Forge: Fix MC-94054
        double d1 = Math.abs(this.mob.getY() - (double)vector3i.getY());
        double d2 = Math.abs(this.mob.getZ() - ((double)vector3i.getZ() + (this.mob.getBbWidth() + 1) / 2D)); //Forge: Fix MC-94054
        boolean flag = d0 <= (double)this.maxDistanceToWaypoint && d2 <= (double)this.maxDistanceToWaypoint && d1 < 1.0D; //Forge: Fix MC-94054
        if (flag || this.mob.canCutCorner(this.path.getNextNode().type) && this.shouldTargetNextNodeInDirection(tempMobPosition)) {
            this.path.advance();
        }

        this.doStuckDetection(tempMobPosition);
    }

    private boolean shouldTargetNextNodeInDirection(Vec3 currentPosition) {
        if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
            return false;
        } else {
            Vec3 nextNodePosition = Vec3.atBottomCenterOf(this.path.getNextNodePos());
            if (!currentPosition.closerThan(nextNodePosition, 2.0D)) {
                return false;
            } else {
                Vec3 next_nextPos = Vec3.atBottomCenterOf(this.path.getNodePos(this.path.getNextNodeIndex() + 1));
                Vec3 deltaPos = next_nextPos.subtract(nextNodePosition);
                Vec3 vector3d3 = currentPosition.subtract(nextNodePosition);
                double result = deltaPos.dot(vector3d3);
                return result > 0.0D;
            }
        }
    }

    protected PathFinder createPathFinder(int p_179679_1_) {
        this.nodeEvaluator = new CompanionWalkerNodeProcessor();
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanOpenDoors(true);
        return new PathFinder(this.nodeEvaluator, p_179679_1_);
    }

    protected boolean hasValidPathType(BlockPathTypes p_230287_1_) {
        if (p_230287_1_ == BlockPathTypes.WATER) {
            return true;
        } else if (p_230287_1_ == BlockPathTypes.LAVA) {
            return false;
        } else {
            return p_230287_1_ != BlockPathTypes.OPEN;
        }
    }


    @Override
    public boolean isStableDestination(BlockPos p_188555_1_) {

        return (this.companion.canUseRigging() && this.level.getBlockState(p_188555_1_).is(Blocks.WATER)) || super.isStableDestination(p_188555_1_);
    }
}

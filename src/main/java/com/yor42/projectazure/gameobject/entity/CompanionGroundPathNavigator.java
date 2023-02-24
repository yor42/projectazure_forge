package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.pathfinding.CompanionWalkerNodeProcessor;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

public class CompanionGroundPathNavigator extends GroundPathNavigator {

    protected boolean shouldAvoidSun;
    private final AbstractEntityCompanion companion;

    public CompanionGroundPathNavigator(AbstractEntityCompanion entity, World world) {
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
                Vector3d vector3d = this.getTempMobPos();
                Vector3d vector3d1 = path.getNextEntityPos(this.mob);
                if (((this.mob.onClimbable() && Math.abs(vector3d.y - vector3d1.y)<0.3) || vector3d.y > vector3d1.y) && !this.mob.isOnGround() && MathHelper.floor(vector3d.x) == MathHelper.floor(vector3d1.x) && MathHelper.floor(vector3d.z) == MathHelper.floor(vector3d1.z)) {
                    path.advance();
                }
            }

            DebugPacketSender.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
            if (!this.isDone()) {
                Vector3d vector3d2 = this.path.getNextEntityPos(this.mob);
                BlockPos blockpos = new BlockPos(vector3d2);
                double y = this.level.getBlockState(blockpos.below()).getMaterial() == Material.AIR ? vector3d2.y : WalkNodeProcessor.getFloorLevel(this.level, blockpos);
                this.mob.getMoveControl().setWantedPosition(vector3d2.x, y, vector3d2.z, this.speedModifier);
            }
        }
    }

    protected void followThePath() {
        Vector3d tempMobPosition = this.getTempMobPos();
        this.maxDistanceToWaypoint = this.mob.getBbWidth() > 0.75F ? this.mob.getBbWidth() / 2.0F : 0.75F - this.mob.getBbWidth() / 2.0F;
        Vector3i vector3i = this.path.getNextNodePos();
        double d0 = Math.abs(this.mob.getX() - ((double)vector3i.getX() + (this.mob.getBbWidth() + 1) / 2D)); //Forge: Fix MC-94054
        double d1 = Math.abs(this.mob.getY() - (double)vector3i.getY());
        double d2 = Math.abs(this.mob.getZ() - ((double)vector3i.getZ() + (this.mob.getBbWidth() + 1) / 2D)); //Forge: Fix MC-94054
        boolean flag = d0 <= (double)this.maxDistanceToWaypoint && d2 <= (double)this.maxDistanceToWaypoint && d1 < 1.0D; //Forge: Fix MC-94054
        if (flag || this.mob.canCutCorner(this.path.getNextNode().type) && this.shouldTargetNextNodeInDirection(tempMobPosition)) {
            this.path.advance();
        }

        this.doStuckDetection(tempMobPosition);
    }

    private boolean shouldTargetNextNodeInDirection(Vector3d currentPosition) {
        if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
            return false;
        } else {
            Vector3d nextNodePosition = Vector3d.atBottomCenterOf(this.path.getNextNodePos());
            if (!currentPosition.closerThan(nextNodePosition, 2.0D)) {
                return false;
            } else {
                Vector3d next_nextPos = Vector3d.atBottomCenterOf(this.path.getNodePos(this.path.getNextNodeIndex() + 1));
                Vector3d deltaPos = next_nextPos.subtract(nextNodePosition);
                Vector3d vector3d3 = currentPosition.subtract(nextNodePosition);
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

    protected boolean hasValidPathType(PathNodeType p_230287_1_) {
        if (p_230287_1_ == PathNodeType.WATER) {
            return true;
        } else if (p_230287_1_ == PathNodeType.LAVA) {
            return false;
        } else {
            return p_230287_1_ != PathNodeType.OPEN;
        }
    }


    @Override
    public boolean isStableDestination(BlockPos p_188555_1_) {

        return (this.companion.canUseRigging() && this.level.getBlockState(p_188555_1_).is(Blocks.WATER)) || super.isStableDestination(p_188555_1_);
    }
}

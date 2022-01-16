package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class CompanionFollowOwnerGoal extends FollowOwnerGoal {

    private final AbstractEntityCompanion host;
    private final double speed;
    private boolean shouldTeleport;
    private LivingEntity owner;

    private final float mindist;

    private int pathRefreshTick;

    public CompanionFollowOwnerGoal(AbstractEntityCompanion tameable, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
        super(tameable, speed, minDist, maxDist, teleportToLeaves);
        this.shouldTeleport = teleportToLeaves;
        this.speed = speed;
        this.host = tameable;
        this.mindist = minDist;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        super.canUse();
        LivingEntity livingentity = this.host.getOwner();

        if(livingentity == null||livingentity.isSpectator() || livingentity.getCommandSenderWorld() != this.host.getCommandSenderWorld() ||this.host.isOrderedToSit()||this.host.distanceToSqr(livingentity) < (double)(this.mindist * this.mindist)||this.host.isFreeRoaming() || this.host.isSleeping() || this.host.isMovingtoRecruitStation){
            return false;
        }
        else{
            this.owner = livingentity;
            return true;
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    public void tick() {


        if (this.owner != null) {
            boolean val = this.host.isSwimming();

            boolean cond1 = this.host.distanceTo(this.owner) >= 5.0D;
            boolean cond2 = !this.host.isLeashed();
            boolean cond3 = !this.host.isPassenger();

            if (cond1&&cond2&&cond3) {
                //teleportToOwner();
                this.host.getLookControl().setLookAt(this.owner, 10.0F, (float) this.host.getMaxHeadXRot());
                this.host.getNavigation().moveTo(this.owner, this.speed);
                if(!val) {
                    this.host.setSprinting(this.host.distanceTo(this.owner) > 7.0);
                }

                if(this.host.isSwimming()){
                    if(this.host.getNavigation().createPath(this.owner, 0) == null){
                        this.host.setSwimmingUp(true);
                    }
                    else if(this.host.isSwimmingUp()){
                        this.host.setSwimmingUp(false);
                    }
                }

                if(this.host.distanceTo(this.owner)>20){
                    this.tryToTeleportNearEntity();
                }

            }
        }
    }

    private void tryToTeleportNearEntity() {
        BlockPos blockpos = this.owner.blockPosition();

        for(int i = 0; i < 10; ++i) {
            int j = this.getRandomNumber(-3, 3);
            int k = this.getRandomNumber(-1, 1);
            int l = this.getRandomNumber(-3, 3);
            boolean flag = this.tryToTeleportToLocation(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if (flag) {
                return;
            }
        }

    }

    private int getRandomNumber(int min, int max) {
        return this.host.getRandom().nextInt(max - min + 1) + min;
    }

    private boolean tryToTeleportToLocation(int x, int y, int z) {
        if (Math.abs((double)x - this.owner.getX()) < 2.0D && Math.abs((double)z - this.owner.getZ()) < 2.0D) {
            return false;
        } else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
            return false;
        } else {
            this.host.moveTo((double)x + 0.5D, (double)y, (double)z + 0.5D, this.host.yRot, this.host.xRot);
            this.host.getNavigation().stop();
            return true;
        }
    }

    private boolean isTeleportFriendlyBlock(BlockPos pos) {
        PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic(host.getCommandSenderWorld(), pos.mutable());
        if (pathnodetype != PathNodeType.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = this.host.getCommandSenderWorld().getBlockState(pos.below());
            if (!this.shouldTeleport && blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pos.subtract(this.host.blockPosition());
                return this.host.getCommandSenderWorld().noCollision(this.host, this.host.getBoundingBox().move(blockpos));
            }
        }
    }

    private void teleportToOwner()
    {
        BlockPos blockpos = this.owner.blockPosition();
        for(int i = 0; i < 10; ++i)
        {
            int j = this.getRandBetween(-3, 3);
            int k = this.getRandBetween(-1, 1);
            int l = this.getRandBetween(-3, 3);
            boolean flag = this.maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if(flag)
            {
                return;
            }
        }
    }

    private boolean maybeTeleportTo(int p_226328_1_, int p_226328_2_, int p_226328_3_)
    {
        if(Math.abs((double)p_226328_1_ - this.host.getOwner().getX()) < 2.0D && Math.abs((double)p_226328_3_ - this.host.getOwner().getZ()) < 2.0D)
        {
            return false;
        }
        else if(!this.canTeleportTo(new BlockPos(p_226328_1_, p_226328_2_, p_226328_3_)))
        {
            return false;
        }
        else
        {
            this.host.moveTo((double)p_226328_1_ + 0.5D, (double)p_226328_2_, (double)p_226328_3_ + 0.5D, this.host.yRot, this.host.xRot);
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos pos)
    {
        PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic(this.host.getCommandSenderWorld(), pos.mutable());
        if(pathnodetype == PathNodeType.DANGER_FIRE || pathnodetype == PathNodeType.DAMAGE_FIRE)
        {
            return false;
        }
        else
        {
            BlockState blockstate = this.host.getCommandSenderWorld().getBlockState(pos.below());
            if(blockstate.getBlock() instanceof AirBlock || blockstate.getBlock() == Blocks.LAVA)
            {
                return false;
            }
            else
            {
                BlockPos blockpos = pos.subtract(this.host.blockPosition());
                return this.host.getCommandSenderWorld().noCollision(this.host, this.host.getBoundingBox().move(blockpos));
            }
        }
    }

    private int getRandBetween(int p_226327_1_, int p_226327_2_)
    {
        return this.host.getRandom().nextInt(p_226327_2_ - p_226327_1_ + 1) + p_226327_1_;
    }
}

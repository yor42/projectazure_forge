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
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
    }

    public boolean shouldExecute() {
        super.shouldExecute();
        LivingEntity livingentity = this.host.getOwner();

        if(livingentity == null||livingentity.isSpectator() || livingentity.getEntityWorld() != this.host.getEntityWorld() ||this.host.isSitting()||this.host.getDistanceSq(livingentity) < (double)(this.mindist * this.mindist)||this.host.isFreeRoaming() || this.host.isSleeping() || this.host.isMovingtoRecruitStation){
            return false;
        }
        else{
            this.owner = livingentity;
            return true;
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
    }

    public void tick() {


        if (this.owner != null) {
            boolean val = this.host.isSwimming();

            boolean cond1 = this.host.getDistance(this.owner) >= 5.0D;
            boolean cond2 = !this.host.getLeashed();
            boolean cond3 = !this.host.isPassenger();

            if (cond1&&cond2&&cond3) {
                //func_226330_g_();
                this.host.getLookController().setLookPositionWithEntity(this.owner, 10.0F, (float) this.host.getVerticalFaceSpeed());
                this.host.getNavigator().tryMoveToEntityLiving(this.owner, this.speed);
                if(!val) {
                    this.host.setSprinting(this.host.getDistance(this.owner) > 7.0);
                }

                if(this.host.isSwimming()){
                    if(this.host.getNavigator().getPathToEntity(this.owner, 0) == null){
                        this.host.setSwimmingUp(true);
                    }
                    else if(this.host.isSwimmingUp()){
                        this.host.setSwimmingUp(false);
                    }
                }

                if(this.host.getDistance(this.owner)>20){
                    this.tryToTeleportNearEntity();
                }

            }
        }
    }

    private void tryToTeleportNearEntity() {
        BlockPos blockpos = this.owner.getPosition();

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
        return this.host.getRNG().nextInt(max - min + 1) + min;
    }

    private boolean tryToTeleportToLocation(int x, int y, int z) {
        if (Math.abs((double)x - this.owner.getPosX()) < 2.0D && Math.abs((double)z - this.owner.getPosZ()) < 2.0D) {
            return false;
        } else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
            return false;
        } else {
            this.host.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, this.host.rotationYaw, this.host.rotationPitch);
            this.host.getNavigator().clearPath();
            return true;
        }
    }

    private boolean isTeleportFriendlyBlock(BlockPos pos) {
        PathNodeType pathnodetype = WalkNodeProcessor.func_237231_a_(host.getEntityWorld(), pos.toMutable());
        if (pathnodetype != PathNodeType.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = this.host.getEntityWorld().getBlockState(pos.down());
            if (!this.shouldTeleport && blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pos.subtract(this.host.getPosition());
                return this.host.getEntityWorld().hasNoCollisions(this.host, this.host.getBoundingBox().offset(blockpos));
            }
        }
    }

    private void func_226330_g_()
    {
        BlockPos blockpos = this.owner.getPosition();
        for(int i = 0; i < 10; ++i)
        {
            int j = this.getRandBetween(-3, 3);
            int k = this.getRandBetween(-1, 1);
            int l = this.getRandBetween(-3, 3);
            boolean flag = this.func_226328_a_(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if(flag)
            {
                return;
            }
        }
    }

    private boolean func_226328_a_(int p_226328_1_, int p_226328_2_, int p_226328_3_)
    {
        if(Math.abs((double)p_226328_1_ - this.host.getOwner().getPosX()) < 2.0D && Math.abs((double)p_226328_3_ - this.host.getOwner().getPosZ()) < 2.0D)
        {
            return false;
        }
        else if(!this.func_226329_a_(new BlockPos(p_226328_1_, p_226328_2_, p_226328_3_)))
        {
            return false;
        }
        else
        {
            this.host.setLocationAndAngles((double)p_226328_1_ + 0.5D, (double)p_226328_2_, (double)p_226328_3_ + 0.5D, this.host.rotationYaw, this.host.rotationPitch);
            return true;
        }
    }

    private boolean func_226329_a_(BlockPos pos)
    {
        PathNodeType pathnodetype = WalkNodeProcessor.func_237231_a_(this.host.getEntityWorld(), pos.toMutable());
        if(pathnodetype == PathNodeType.DANGER_FIRE || pathnodetype == PathNodeType.DAMAGE_FIRE)
        {
            return false;
        }
        else
        {
            BlockState blockstate = this.host.getEntityWorld().getBlockState(pos.down());
            if(blockstate.getBlock() instanceof AirBlock || blockstate.getBlock() == Blocks.LAVA)
            {
                return false;
            }
            else
            {
                BlockPos blockpos = pos.subtract(this.host.getPosition());
                return this.host.getEntityWorld().hasNoCollisions(this.host, this.host.getBoundingBox().offset(blockpos));
            }
        }
    }

    private int getRandBetween(int p_226327_1_, int p_226327_2_)
    {
        return this.host.getRNG().nextInt(p_226327_2_ - p_226327_1_ + 1) + p_226327_1_;
    }
}
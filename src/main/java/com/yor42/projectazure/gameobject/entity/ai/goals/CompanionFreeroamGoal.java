package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class CompanionFreeroamGoal extends RandomWalkingGoal {

    private final AbstractEntityCompanion entityCompanion;
    private final boolean shouldStopBeforeMove;

    public CompanionFreeroamGoal(AbstractEntityCompanion entity, int moveChance, boolean shouldStopBeforeMove) {
        super(entity, 0.6D, moveChance, shouldStopBeforeMove);
        this.entityCompanion = entity;
        this.shouldStopBeforeMove = shouldStopBeforeMove;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {

        if ((this.entityCompanion.isBeingPatted() || !this.entityCompanion.isFreeRoaming()) && !this.entityCompanion.isMovingtoRecruitStation) {
            return false;
        }
        if (this.entityCompanion.isSleeping()) {
            return false;
        }

        if (this.mob.isVehicle()) {
            return false;
        } else if (!this.forceTrigger) {
            if (this.shouldStopBeforeMove && this.mob.getNoActionTime() >= 100) {
                return false;
            }

            if (this.mob.getRandom().nextInt(this.interval) != 0) {
                return false;
            }
        }

        Vector3d vector3d = this.getPosition();
        if (vector3d == null) {
            return false;
        } else {
            Path path = this.entityCompanion.getNavigation().createPath(vector3d.x,vector3d.y, vector3d.z, 0);
            if (path == null || !path.canReach()){
                return false;
            }
            else {
                this.wantedX = vector3d.x;
                this.wantedY = vector3d.y;
                this.wantedZ = vector3d.z;
                this.forceTrigger = false;
                return true;
            }
        }
    }



    @Nullable
    @Override
    protected Vector3d getPosition() {

        if (!this.entityCompanion.getStayCenterPos().isPresent()){
            this.entityCompanion.setStayCenterPos(this.entityCompanion.blockPosition());
        }

        BlockPos homepos = this.entityCompanion.getStayCenterPos().get();
        BlockPos originPosition;
        boolean flag = false;

        if (this.entityCompanion.isWithinRestriction()) {
            homepos = this.entityCompanion.getRestrictCenter();
            flag = this.entityCompanion.getCommandSenderWorld().dimension() == World.OVERWORLD && this.entityCompanion.blockPosition().closerThan(homepos, 32);
        }

        originPosition = flag? homepos : this.entityCompanion.getStayCenterPos().get();

        int x = ((int)(getRand().nextFloat()*20)-10);
        int z = ((int)(getRand().nextFloat()*20)-10);

        return new Vector3d(originPosition.getX()+x, originPosition.getY(), originPosition.getZ()+z);
    }

    public void stop() {
        this.entityCompanion.clearStayCenterPos();
        this.entityCompanion.getNavigation().stop();
        super.stop();
    }
}
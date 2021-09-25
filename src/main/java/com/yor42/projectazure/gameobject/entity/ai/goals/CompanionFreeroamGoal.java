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

public class CompanionFreeroamGoal extends RandomWalkingGoal {

    private final AbstractEntityCompanion entityCompanion;
    private final boolean shouldStopBeforeMove;

    public CompanionFreeroamGoal(AbstractEntityCompanion entity, int moveChance, boolean shouldStopBeforeMove) {
        super(entity, 0.6D, moveChance, shouldStopBeforeMove);
        this.entityCompanion = entity;
        this.shouldStopBeforeMove = shouldStopBeforeMove;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {

        if ((this.entityCompanion.isBeingPatted() || !this.entityCompanion.isFreeRoaming()) && !this.entityCompanion.isMovingtoRecruitStation) {
            return false;
        }
        if (this.entityCompanion.isSleeping()) {
            return false;
        }

        if (this.creature.isBeingRidden()) {
            return false;
        } else if (!this.mustUpdate) {
            if (this.shouldStopBeforeMove && this.creature.getIdleTime() >= 100) {
                return false;
            }

            if (this.creature.getRNG().nextInt(this.executionChance) != 0) {
                return false;
            }
        }

        Vector3d vector3d = this.getPosition();
        if (vector3d == null) {
            return false;
        } else {
            Path path = this.entityCompanion.getNavigator().getPathToPos(vector3d.x,vector3d.y, vector3d.z, 0);
            if (path == null || !path.reachesTarget()){
                return false;
            }
            else {
                this.x = vector3d.x;
                this.y = vector3d.y;
                this.z = vector3d.z;
                this.mustUpdate = false;
                return true;
            }
        }
    }



    @Nullable
    @Override
    protected Vector3d getPosition() {

        if (!this.entityCompanion.getStayCenterPos().isPresent()){
            this.entityCompanion.setStayCenterPos(this.entityCompanion.getPosition());
        }

        BlockPos homepos = this.entityCompanion.getStayCenterPos().get();
        BlockPos originPosition;
        boolean flag = false;

        if (this.entityCompanion.isWithinHomeDistanceCurrentPosition()) {
            homepos = this.entityCompanion.getHomePosition();
            flag = this.entityCompanion.getEntityWorld().getDimensionKey() == World.OVERWORLD && this.entityCompanion.getPosition().withinDistance(homepos, 32);
        }

        originPosition = flag? homepos : this.entityCompanion.getStayCenterPos().get();

        int x = ((int)(getRand().nextFloat()*20)-10);
        int z = ((int)(getRand().nextFloat()*20)-10);

        return new Vector3d(originPosition.getX()+x, originPosition.getY(), originPosition.getZ()+z);
    }

    public void resetTask() {
        this.entityCompanion.clearStayCenterPos();
        this.entityCompanion.getNavigator().clearPath();
        super.resetTask();
    }
}
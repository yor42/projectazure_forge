package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;

public class CompanionFreeroamGoal extends RandomWalkingGoal {

    private AbstractEntityCompanion entityCompanion;

    public CompanionFreeroamGoal(AbstractEntityCompanion entity, int moveChance, boolean shouldStopBeforeMove){
        super(entity, 0.6D, moveChance,shouldStopBeforeMove);
        this.entityCompanion = entity;
    }

    @Override
    public boolean shouldExecute() {

        if(this.entityCompanion.isBeingPatted()||!this.entityCompanion.isFreeRoaming()){
            return false;
        }
        return super.shouldExecute() && !this.entityCompanion.isSleeping();
    }

    @Nullable
    @Override
    protected Vector3d getPosition() {

        if (this.entityCompanion.getStayCenterPos() == BlockPos.ZERO){
            this.entityCompanion.setStayCenterPos(this.entityCompanion.getPosition());
        }

        BlockPos homepos = this.entityCompanion.getStayCenterPos();
        BlockPos originPosition;
        boolean flag = false;

        if (this.entityCompanion.isWithinHomeDistanceCurrentPosition()) {
            homepos = this.entityCompanion.getHomePosition();
            flag = this.entityCompanion.getEntityWorld().getDimensionKey() == World.OVERWORLD && this.entityCompanion.getPosition().withinDistance(homepos, 32);
        }

        originPosition = flag? homepos : this.entityCompanion.getStayCenterPos();

        int x = ((int)(getRand().nextFloat()*20)-10);
        int z = ((int)(getRand().nextFloat()*20)-10);

        return new Vector3d(originPosition.getX()+x, originPosition.getY(), originPosition.getZ()+z);
    }

    public void resetTask() {
        this.entityCompanion.getNavigator().clearPath();
        super.resetTask();
    }
}

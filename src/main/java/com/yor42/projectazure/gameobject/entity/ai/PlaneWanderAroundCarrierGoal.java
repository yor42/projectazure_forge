package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class PlaneWanderAroundCarrierGoal extends Goal {

    AbstractEntityPlanes plane;

    public PlaneWanderAroundCarrierGoal(AbstractEntityPlanes plane){
        this.plane = plane;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        return this.plane.getAttackTarget() == null && this.plane.hasPayload()&& !this.plane.isOnBombRun() && this.plane.getOwner() != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.plane.getAttackTarget() == null && this.plane.hasPayload()&& !this.plane.isOnBombRun() && this.plane.getOwner() != null;
    }

    @Override
    public void tick() {
        super.tick();
        MovementController movehelper = this.plane.getMoveHelper();
        if(this.plane.getDistanceSq(movehelper.getX(), movehelper.getY(), movehelper.getZ())<2.0F) {
            Vector3d pos;
            if (this.plane.getOwner() != null) {
                Vector3d carrierLoc = this.plane.getOwner().getPositionVec();
                Vector3d vector3d = carrierLoc.subtract(this.plane.getPositionVec()).normalize();
                //currently same as bee wandering
                pos = RandomPositionGenerator.findAirTarget(this.plane, 8, 7, vector3d, ((float) Math.PI / 2F), 2, 1);
            } else {
                pos = RandomPositionGenerator.findRandomTarget(this.plane, 15, 15);
            }
            if (pos != null) {
                movehelper.setMoveTo(pos.getX(), pos.getY(), pos.getZ(), 1.0F);
            }
        }
    }
}

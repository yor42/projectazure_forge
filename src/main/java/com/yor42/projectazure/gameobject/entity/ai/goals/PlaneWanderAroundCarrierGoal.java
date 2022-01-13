package com.yor42.projectazure.gameobject.entity.ai.goals;

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
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return (this.plane.getTarget() == null || !this.plane.getSensing().canSee(this.plane.getTarget())) && this.plane.hasPayload()&& !this.plane.isOnBombRun() && this.plane.getOwner() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.plane.getTarget() == null && this.plane.hasPayload()&& !this.plane.isOnBombRun() && this.plane.getOwner() != null;
    }

    @Override
    public void tick() {
        super.tick();
        MovementController movehelper = this.plane.getMoveControl();
        if(this.plane.distanceToSqr(movehelper.getWantedX(), movehelper.getWantedY(), movehelper.getWantedZ())<2.0F) {
            Vector3d pos;
            if (this.plane.getOwner() != null) {
                Vector3d carrierLoc = this.plane.getOwner().position();
                Vector3d vector3d = carrierLoc.subtract(this.plane.position()).normalize();
                //currently same as bee wandering
                pos = RandomPositionGenerator.getAboveLandPos(this.plane, 8, 7, vector3d, ((float) Math.PI / 2F), 2, 1);
            } else {
                pos = RandomPositionGenerator.getPos(this.plane, 15, 15);
            }
            if (pos != null) {
                movehelper.setWantedPosition(pos.x(), pos.y(), pos.z(), 1.0F);
            }
        }
    }
}

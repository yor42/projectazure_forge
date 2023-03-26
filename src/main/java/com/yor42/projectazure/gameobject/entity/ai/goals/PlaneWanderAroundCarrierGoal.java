package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.planes.AbstractEntityPlanes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class PlaneWanderAroundCarrierGoal extends Goal {

    AbstractEntityPlanes plane;

    public PlaneWanderAroundCarrierGoal(AbstractEntityPlanes plane){
        this.plane = plane;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return (this.plane.getTarget() == null || !this.plane.getSensing().hasLineOfSight(this.plane.getTarget())) && this.plane.hasPayload()&& !this.plane.isOnBombRun() && this.plane.getOwner() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.plane.getTarget() == null && this.plane.hasPayload()&& !this.plane.isOnBombRun() && this.plane.getOwner() != null;
    }

    @Override
    public void tick() {
        super.tick();
        MoveControl movehelper = this.plane.getMoveControl();
        if(this.plane.distanceToSqr(movehelper.getWantedX(), movehelper.getWantedY(), movehelper.getWantedZ())<2.0F) {
        }
    }
}

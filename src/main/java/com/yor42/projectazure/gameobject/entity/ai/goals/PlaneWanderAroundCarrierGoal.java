package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

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
            Vec3 pos;
            if (this.plane.getOwner() != null) {
                Vec3 carrierLoc = this.plane.getOwner().position();
                //currently same as bee wandering
                pos = LandRandomPos.getPosTowards(this.plane, 8, 7, carrierLoc);
            } else {
                pos = LandRandomPos.getPos(this.plane, 15, 15);
            }
            if (pos != null) {
                movehelper.setWantedPosition(pos.x(), pos.y(), pos.z(), 1.0F);
            }
        }
    }
}

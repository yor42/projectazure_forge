package com.yor42.projectazure.gameobject.entity.ai.targetAI;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;

public class PlaneInterceptGoal extends NearestAttackableTargetGoal<AbstractEntityPlanes> {

    AbstractEntityPlanes planes;

    public PlaneInterceptGoal(AbstractEntityPlanes plane) {
        super(plane, AbstractEntityPlanes.class,!plane.hasRadar());
        this.planes = plane;
    }

    @Override
    public boolean shouldExecute() {
        if(this.planes.getPlaneType() != enums.PLANE_TYPE.FIGHTER || !this.planes.canAttack()){
            return false;
        }

        if (this.targetChance > 0 && this.goalOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        } else {
            this.findNearestTarget();
            if(this.nearestTarget == null)
                return false;
            if(this.nearestTarget instanceof AbstractEntityPlanes) {
                return this.planes.getOwner() != ((AbstractEntityPlanes) this.nearestTarget).getOwner() && this.planes.getOwner().getOwner() != ((AbstractEntityPlanes) this.nearestTarget).getOwner().getOwner();
            }
        }
        return false;
    }
}

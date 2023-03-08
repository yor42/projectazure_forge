package com.yor42.projectazure.gameobject.entity.ai.targetAI;

import com.yor42.projectazure.gameobject.entity.planes.AbstractEntityPlanes;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;

public class PlaneInterceptGoal extends NearestAttackableTargetGoal<AbstractEntityPlanes> {

    AbstractEntityPlanes planes;

    public PlaneInterceptGoal(AbstractEntityPlanes plane) {
        super(plane, AbstractEntityPlanes.class,!plane.hasRadar());
        this.planes = plane;
    }

    @Override
    public boolean canUse() {
        if(this.planes.getPlaneType() != enums.PLANE_TYPE.FIGHTER || !this.planes.canAttack()){
            return false;
        }

        if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
            return false;
        } else {
            this.findTarget();
            if(this.target == null)
                return false;
            if(this.target instanceof AbstractEntityPlanes) {
                if(((AbstractEntityPlanes) this.target).getOwner()==null){
                    return true;
                }

                boolean isDifferentCompanionOwner = this.planes.getOwner().getOwner() != ((AbstractEntityPlanes) this.target).getOwner().getOwner();
                boolean isDifferentPlaneOwner = this.planes.getOwner() != ((AbstractEntityPlanes) this.target).getOwner();
                return isDifferentPlaneOwner && isDifferentCompanionOwner;
            }
        }
        return false;
    }
}

package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;

public class KansenLaunchPlaneGoal extends Goal {

    private EntityKansenBase entity;

    @Override
    public boolean shouldExecute() {
        return false;
    }

    public int CalculateRequiredFuel(AbstractEntityPlanes planes){
        //Movement speed 0.1 = roughly 0.2 block/tick
        if(this.entity.getAttackTarget() != null && this.entity.getAttackTarget().isAlive()) {
            double distance = entity.getDistance(this.entity.getAttackTarget())*2.5;//2 way trip+ another 0.5X of fuel because minecraft entity are stupid
            double speed = planes.getAttributeValue(Attributes.MOVEMENT_SPEED);
            return (int)Math.ceil(Math.abs(distance/speed));
        }
        return 0;
    }

}

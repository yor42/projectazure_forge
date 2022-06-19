package com.yor42.projectazure.gameobject.entity.ai.goals.planes;

import com.yor42.projectazure.gameobject.entity.planes.AbstractEntityPlanes;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public abstract class PlaneMoveGoal extends Goal {
    private final AbstractEntityPlanes entity;
    public PlaneMoveGoal(AbstractEntityPlanes entity) {
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.entity = entity;
    }

    protected boolean touchingTarget() {
        return this.entity.moveTargetPoint.distanceToSqr(this.entity.getX(), this.entity.getY(), this.entity.getZ()) < 4.0D;
    }
}

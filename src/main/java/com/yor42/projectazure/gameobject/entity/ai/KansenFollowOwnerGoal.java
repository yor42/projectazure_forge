package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;

public class KansenFollowOwnerGoal extends FollowOwnerGoal {

    private EntityKansenBase host;
    private LivingEntity owner;

    private final float mindist;

    private int pathRefreshTick;

    public KansenFollowOwnerGoal(EntityKansenBase tameable, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
        super(tameable, speed, minDist, maxDist, teleportToLeaves);
        this.host = tameable;
        this.mindist = minDist;
    }

    public boolean shouldExecute() {
        super.shouldExecute();
        LivingEntity livingentity = this.host.getOwner();
        if (livingentity == null) {
            return false;
        } else if (livingentity.isSpectator()) {
            return false;
        } else if (this.host.isSitting()) {
            return false;
        } else if (this.host.getDistanceSq(livingentity) < (double)(this.mindist * this.mindist)) {
            return false;
        } else {
            this.owner = livingentity;
            return true;
        }
    }

    @Override
    public void resetTask() {
        this.host.setSprinting(false);
        super.resetTask();
    }

    @Override
    public void tick() {
        if(--this.pathRefreshTick <= 0) {
            this.pathRefreshTick = 10;
            if(!this.host.isSailing()) {
                this.host.setSprinting(this.host.getDistanceSq(this.owner) >= 64);
            }
        }
        super.tick();
    }
}

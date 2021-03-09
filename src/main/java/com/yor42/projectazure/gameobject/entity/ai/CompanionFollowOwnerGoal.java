package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;

public class CompanionFollowOwnerGoal extends FollowOwnerGoal {

    private AbstractEntityCompanion host;
    private LivingEntity owner;

    private final float mindist;

    private int pathRefreshTick;

    public CompanionFollowOwnerGoal(AbstractEntityCompanion tameable, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
        super(tameable, speed, minDist, maxDist, teleportToLeaves);
        this.host = tameable;
        this.mindist = minDist;
    }

    public boolean shouldExecute() {
        super.shouldExecute();
        LivingEntity livingentity = this.host.getOwner();

        if(livingentity == null||livingentity.isSpectator()||this.host.isSitting()||this.host.getDistanceSq(livingentity) < (double)(this.mindist * this.mindist)||this.host.isFreeRoaming() || this.host.isSleeping()){
            return false;
        }
        else{
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
            boolean canRun = !(this.host instanceof EntityKansenBase && ((EntityKansenBase) this.host).isSailing()) && this.host.isOnGround();
            this.host.setSprinting(this.host.getDistance(this.owner) >= 6 && canRun);
        }
        super.tick();
    }
}

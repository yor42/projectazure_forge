package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.BoatEntity;
public class KansenRideBoatAlongPlayerGoal extends Goal {

    private final AbstractEntityCompanion entity;
    private final double movespeed;

    public KansenRideBoatAlongPlayerGoal(AbstractEntityCompanion entity, double speed){
        this.entity = entity;
        this.movespeed = speed;
    }

    @Override
    public boolean shouldExecute() {

        Entity Owner = this.entity.getOwner();

        if(Owner == null){
            return false;
        }

        if(Owner.getRidingEntity() == null){
            return false;
        }

        Entity Boat = Owner.getRidingEntity();

        boolean flag = Boat instanceof BoatEntity && Boat.isOnePlayerRiding();

        if (flag) {
            return !this.entity.canUseRigging() && !this.entity.isEntitySleeping();
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if(this.entity.getOwner() != null)
            return this.entity.getOwner().getRidingEntity() != null;
        return false;
    }

    @Override
    public void resetTask() {
        this.entity.stopRiding();
        super.resetTask();
    }

    @Override
    public void tick() {
        if (this.entity.getOwner() != null) {
            BoatEntity Boat = (BoatEntity) this.entity.getOwner().getRidingEntity();
            if (Boat != null) {
                if (Boat.isOnePlayerRiding()) {
                    this.entity.getLookController().setLookPositionWithEntity(Boat, 30.0F, 30.0F);
                    this.entity.getNavigator().tryMoveToEntityLiving(Boat, this.movespeed);
                    double distanceSq = this.entity.getDistanceSq(Boat.getPosX(), Boat.getPosY(), Boat.getPosZ());
                    if (distanceSq <= 3.0) {
                        this.entity.startRiding(Boat);
                    }
                }
            }
        }
    }
}

package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.tags.FluidTags;
import java.util.List;

import net.minecraft.util.math.MathHelper;
import org.lwjgl.system.CallbackI;

import java.util.Map.Entry;
import java.util.function.Predicate;

public class KansenRideBoatAlongPlayerGoal extends Goal {

    private final AbstractEntityCompanion entity;
    private Entity boat;
    private boolean sameBoatWithOwner;
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

        boolean flag = Boat instanceof BoatEntity && Boat.getPassengers().size()<2;

        if (flag) {
            this.boat = Boat;
            this.sameBoatWithOwner = true;
            return !this.entity.canUseRigging() && !this.entity.isEntitySleeping();
        }
        else{
            List<Entity> possibleBoat = this.entity.getEntityWorld().getEntitiesInAABBexcluding(this.entity, this.entity.getBoundingBox().expand(10, 2, 10), (entity) -> entity instanceof BoatEntity && entity.getPassengers().size() < 2 && entity.areEyesInFluid(FluidTags.WATER));
            if(!possibleBoat.isEmpty()) {
                this.boat = possibleBoat.get(possibleBoat.size() <= 1 ? 0 : this.entity.getRNG().nextInt(possibleBoat.size()));
                if (this.boat != null) {
                    this.sameBoatWithOwner = false;
                    return true;
                }
            }
        }
        this.sameBoatWithOwner = false;
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
        this.boat = null;
        this.sameBoatWithOwner = false;
        this.entity.stopRiding();
        super.resetTask();
    }

    @Override
    public void tick() {

        if(this.entity.ticksExisted%10 == 0){
            Entity Boat = this.entity.getOwner().getRidingEntity();

            boolean flag = Boat instanceof BoatEntity && Boat.getPassengers().size()<2;
            if (flag) {
                this.boat = Boat;
            }
            else{
                List<Entity> possibleBoat = this.entity.getEntityWorld().getEntitiesInAABBexcluding(this.entity, this.entity.getBoundingBox().expand(20, 2, 20), (entity) -> entity instanceof BoatEntity && entity.getPassengers().size() < 2);
                if(!possibleBoat.isEmpty()) {
                    this.boat = possibleBoat.get(possibleBoat.size() <= 1 ? 0 :this.entity.getRNG().nextInt(possibleBoat.size()));
                    if (this.boat != null) {
                        this.sameBoatWithOwner = false;
                    }
                }
            }
        }

        if (this.entity.getOwner() != null) {
            if (this.boat != null && this.boat instanceof BoatEntity) {
                if (this.boat.isOnePlayerRiding() && this.sameBoatWithOwner) {
                    double distanceSq = this.entity.getDistanceSq(this.boat.getPosX(), this.boat.getPosY(), this.boat.getPosZ());
                    if (distanceSq <= 3.0 && !this.entity.isRidingOrBeingRiddenBy(this.boat)) {
                        this.entity.startRiding(this.boat);
                    }
                    else{
                        this.entity.getLookController().setLookPositionWithEntity(this.boat, 30.0F, 30.0F);
                        this.entity.getNavigator().tryMoveToEntityLiving(this.boat, this.movespeed);
                    }
                }else{
                    double distanceSq = this.entity.getDistanceSq(this.boat.getPosX(), this.boat.getPosY(), this.boat.getPosZ());
                    if (distanceSq <= 3.0 && this.entity.getRidingEntity() != this.boat) {
                        if(!this.entity.isRidingOrBeingRiddenBy(this.boat)) {
                            this.entity.startRiding(this.boat);
                        }
                        if(this.entity == this.boat.getControllingPassenger()){
                            this.entity.getNavigator().tryMoveToEntityLiving(this.entity.getOwner(), this.movespeed);
                        }
                    }
                    else if(this.entity.getRidingEntity() == this.boat){
                        this.entity.getLookController().setLookPositionWithEntity(this.entity.getOwner(), 30.0F, 30.0F);
                        controlBoat((BoatEntity) this.boat, this.entity);
                    }
                    else{
                        this.entity.getNavigator().tryMoveToEntityLiving(this.boat, this.movespeed);
                        this.entity.getLookController().setLookPositionWithEntity(this.boat, 30.0F, 30.0F);

                    }
                }
            }
        }
    }

    private void controlBoat(BoatEntity boat, TameableEntity controller) {
        LivingEntity owner = controller.getOwner();
        if(owner != null) {
            double d0 = boat.getPosX() - owner.getPosX();
            double d1 = boat.getPosZ() - owner.getPosZ();
            float f = this.boat.getDistance(owner)>5? 0.04F:0;
            float oldyaw = boat.rotationYaw;
            boat.setMotion(boat.getMotion().add((double)(MathHelper.sin(-boat.rotationYaw * ((float)Math.PI / 180F)) * f), 0.0D, (double)(MathHelper.cos(boat.rotationYaw * ((float)Math.PI / 180F)) * f)));
            boat.rotationYaw = (float) (MathHelper.atan2(d1, d0) * (double) (180F / (float) Math.PI)) + 90.0F;
            float newyaw = boat.rotationYaw;
            boat.setPaddleState(oldyaw-newyaw < -1 || f>0, oldyaw-newyaw>1 || f>0);
        }


    }

}

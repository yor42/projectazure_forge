package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

import java.util.List;

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
        this.entity.setPathPriority(PathNodeType.WATER, -1.0F);
        this.entity.setPathPriority(PathNodeType.WALKABLE, 0F);
        super.resetTask();
    }

    @Override
    public void tick() {
        if (this.entity.getOwner() != null) {
            Entity Boat = this.entity.getOwner().getRidingEntity();

            if (Boat instanceof BoatEntity && this.entity.getRidingEntity() != Boat && Boat.getPassengers().size() >= 2) {
                this.entity.getNavigator().clearPath();

            }

            if (this.entity.ticksExisted % 10 == 0) {

                boolean flag = Boat instanceof BoatEntity && Boat.getPassengers().size() < 2;
                if (flag) {
                    this.boat = Boat;
                    this.sameBoatWithOwner = true;
                } else {
                    List<Entity> possibleBoat = this.entity.getEntityWorld().getEntitiesInAABBexcluding(this.entity, this.entity.getBoundingBox().expand(20, 2, 20), (entity) -> (entity instanceof BoatEntity && ((entity.getPassengers().size() < 2) || (entity.getControllingPassenger() instanceof AbstractEntityCompanion && ((AbstractEntityCompanion) entity.getControllingPassenger()).getOwner() == this.entity.getOwner()))));
                    boolean hasboat = !possibleBoat.isEmpty();

                    if (hasboat) {
                        this.boat = possibleBoat.get(possibleBoat.size() <= 1 ? 0 : this.entity.getRNG().nextInt(possibleBoat.size()));
                        if (this.boat != null) {
                            this.sameBoatWithOwner = false;
                        }
                    }
                }
            }

            if (this.boat != null && this.boat instanceof BoatEntity) {
                if (this.boat.getPassengers().size()<2 && this.sameBoatWithOwner) {
                    double distanceSq = this.entity.getDistanceSq(this.boat.getPosX(), this.boat.getPosY(), this.boat.getPosZ());
                    if (distanceSq <= 3.0 && !this.entity.isRidingOrBeingRiddenBy(this.boat)) {
                        this.entity.startRiding(this.boat);
                    } else {
                        this.entity.getLookController().setLookPositionWithEntity(this.boat, 30.0F, 30.0F);
                        this.entity.getNavigator().tryMoveToEntityLiving(this.boat, this.movespeed);
                    }
                } else {
                    double distanceSq = this.entity.getDistanceSq(this.boat.getPosX(), this.boat.getPosY(), this.boat.getPosZ());
                    if (distanceSq <= 3.0 && this.entity.getRidingEntity() != this.boat) {
                        if (!this.entity.isRidingOrBeingRiddenBy(this.boat)) {
                            this.entity.startRiding(this.boat);
                        }
                        if (this.entity == this.boat.getControllingPassenger()) {
                            this.entity.getNavigator().tryMoveToEntityLiving(this.entity.getOwner(), this.movespeed);
                        }
                    } else if (this.entity.getRidingEntity() == this.boat) {
                        this.entity.getLookController().setLookPositionWithEntity(this.entity.getOwner(), 30.0F, 30.0F);
                        this.entity.setPathPriority(PathNodeType.WATER, 1.0F);
                        this.entity.setPathPriority(PathNodeType.WALKABLE, -1F);
                        Path path = this.entity.getNavigator().getPathToEntity(this.entity.getOwner(), 0);
                        controlBoat((BoatEntity) this.boat, this.entity, path);
                    } else {
                        this.entity.getNavigator().tryMoveToEntityLiving(this.boat, this.movespeed);
                        this.entity.getLookController().setLookPositionWithEntity(this.entity.getOwner(), 30.0F, 30.0F);

                    }
                }
            }
        }
    }

    private void controlBoat(BoatEntity boat, TameableEntity controller, Path path) {
        LivingEntity owner = controller.getOwner();
        if(owner != null && controller == boat.getControllingPassenger()) {
            if(path != null) {

                Vector3d vector3d2 = path.getPosition(this.entity);
                    if (Math.sqrt(controller.getDistanceSq(vector3d2)) < 5) {
                        if(!path.isFinished()) {
                            path.incrementPathIndex();
                        }
                        else{
                            path = controller.getNavigator().getPathToEntity(controller.getOwner(), 0);
                        }
                    }

                /*
                if(Math.sqrt(this.boat.getDistanceSq(vector3d2)) < 3 && !path.isFinished()){
                    Vector3d vector3d = this.entity.getPositionVec();
                    double maxDistanceToWaypoint = 3;
                    Vector3i vector3i = path.func_242948_g();
                    double d0 = Math.abs(this.entity.getPosX() - ((double)vector3i.getX() + (this.entity.getWidth() + 1) / 2D)); //Forge: Fix MC-94054
                    double d2 = Math.abs(this.entity.getPosZ() - ((double)vector3i.getZ() + (this.entity.getWidth() + 1) / 2D)); //Forge: Fix MC-94054
                    boolean flag = d0 < (double)maxDistanceToWaypoint&& d2 < (double)maxDistanceToWaypoint;
                    if (flag || this.entity.func_233660_b_(path.func_237225_h_().nodeType) && this.func_234112_b_(vector3d, path) && path.getCurrentPathIndex()<path.getCurrentPathLength()) {
                        path.incrementPathIndex();
                    }
                }

                 */
                if(!(path != null && path.isFinished()) && controller.getDistance(controller.getOwner())>5) {
                    vector3d2 = path.getPosition(this.entity);
                    BlockPos blockpos = new BlockPos(vector3d2);
                    double d0 = boat.getPosX() - blockpos.getX();
                    double d1 = boat.getPosZ() - blockpos.getZ();
                    float f = 0.04F;
                    float oldyaw = boat.rotationYaw;
                    boat.setMotion(boat.getMotion().add(MathHelper.sin(-boat.rotationYaw * ((float) Math.PI / 180F)) * f, 0.0D, (double) (MathHelper.cos(boat.rotationYaw * ((float) Math.PI / 180F)) * f)));
                    boat.rotationYaw = (float) (MathHelper.atan2(d1, d0) * (double) (180F / (float) Math.PI)) + 90.0F;
                    float newyaw = boat.rotationYaw;
                    float x1 = (float) Math.sqrt(boat.getMotion().getX()*boat.getMotion().getX()+boat.getMotion().getZ()*boat.getMotion().getZ());
                    boat.setPaddleState(oldyaw - newyaw < -0 || x1>0.2, oldyaw - newyaw > 0 || x1>0.2);
                }
            }
        }


    }
    //Shameless copy of vanilla path thing
    private boolean func_234112_b_(Vector3d currentPosition, Path path) {
        if (path.getCurrentPathIndex() + 1 >= path.getCurrentPathLength()) {
            return false;
        } else {
            Vector3d vector3d = Vector3d.copyCenteredHorizontally(path.func_242948_g());
            if (!currentPosition.isWithinDistanceOf(vector3d, 2.0D)) {
                return false;
            } else {
                Vector3d vector3d1 = Vector3d.copyCenteredHorizontally(path.func_242947_d(path.getCurrentPathIndex() + 1));
                Vector3d vector3d2 = vector3d1.subtract(vector3d);
                Vector3d vector3d3 = currentPosition.subtract(vector3d);
                return vector3d2.dotProduct(vector3d3) > 0.0D;
            }
        }
    }

}

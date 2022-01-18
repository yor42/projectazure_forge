package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.List;

public class CompanionRideBoatAlongPlayerGoal extends Goal {

    private final AbstractEntityCompanion entity;
    private Entity boat;
    private boolean sameBoatWithOwner;
    private final double movespeed;

    public CompanionRideBoatAlongPlayerGoal(AbstractEntityCompanion entity, double speed){
        this.entity = entity;
        this.movespeed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {

        Entity Owner = this.entity.getOwner();

        if(Owner == null || this.entity.getCommandSenderWorld() != this.entity.getOwner().getCommandSenderWorld()){
            return false;
        }

        if(Owner.getVehicle() == null){
            return false;
        }

        Entity Boat = Owner.getVehicle();

        boolean flag = Boat instanceof BoatEntity && Boat.getPassengers().size()<2;

        if (flag) {
            this.boat = Boat;
            this.sameBoatWithOwner = true;
            return !this.entity.canUseRigging() && !this.entity.isOrderedToSit();
        }
        else{
            List<Entity> possibleBoat = this.entity.getCommandSenderWorld().getEntities(this.entity, this.entity.getBoundingBox().expandTowards(10, 2, 10), (entity) -> entity instanceof BoatEntity && entity.getPassengers().size() < 2 && entity.isEyeInFluid(FluidTags.WATER));
            if(!possibleBoat.isEmpty()) {
                this.boat = possibleBoat.get(possibleBoat.size() <= 1 ? 0 : this.entity.getRandom().nextInt(possibleBoat.size()));
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
    public boolean canContinueToUse() {
        if(this.entity.getOwner() != null)
            return this.entity.getOwner().getVehicle() != null;
        return false;
    }

    @Override
    public void stop() {
        this.boat = null;
        this.sameBoatWithOwner = false;
        super.stop();
    }

    @Override
    public void tick() {
        if (this.entity.getOwner() != null) {
            Entity Boat = this.entity.getOwner().getVehicle();

            if (Boat instanceof BoatEntity && this.entity.getVehicle() != Boat && Boat.getPassengers().size() >= 2) {
                this.entity.getNavigation().stop();

            }

            if (this.entity.tickCount % 10 == 0) {

                boolean flag = Boat instanceof BoatEntity && Boat.getPassengers().size() < 2;
                if (flag) {
                    this.boat = Boat;
                    this.sameBoatWithOwner = true;
                } else {
                    List<Entity> possibleBoat = this.entity.getCommandSenderWorld().getEntities(this.entity, this.entity.getBoundingBox().expandTowards(20, 2, 20), (entity) -> (entity instanceof BoatEntity && ((entity.getPassengers().size() < 2) || (entity.getControllingPassenger() instanceof AbstractEntityCompanion && ((AbstractEntityCompanion) entity.getControllingPassenger()).getOwner() == this.entity.getOwner()))));
                    boolean hasboat = !possibleBoat.isEmpty();

                    if (hasboat) {
                        this.boat = possibleBoat.get(possibleBoat.size() <= 1 ? 0 : this.entity.getRandom().nextInt(possibleBoat.size()));
                        if (this.boat != null) {
                            this.sameBoatWithOwner = false;
                        }
                    }
                }
            }

            if (this.boat != null && this.boat instanceof BoatEntity && this.entity.getOwner() != null) {
                if (this.boat.getPassengers().size()<2 && this.sameBoatWithOwner) {
                    double distanceSq = this.entity.distanceToSqr(this.boat.getX(), this.boat.getY(), this.boat.getZ());
                    if (distanceSq <= 3.0 && !this.entity.isPassengerOfSameVehicle(this.boat)) {
                        this.entity.startRiding(this.boat);
                    } else {
                        this.entity.getLookControl().setLookAt(this.boat, 30.0F, 30.0F);
                        this.entity.getNavigation().moveTo(this.boat, this.movespeed);
                    }
                } else {
                    double distanceSq = this.entity.distanceToSqr(this.boat.getX(), this.boat.getY(), this.boat.getZ());
                    if (distanceSq <= 3.0 && this.entity.getVehicle() != this.boat) {
                        if (!this.entity.isPassengerOfSameVehicle(this.boat)) {
                            this.entity.startRiding(this.boat);
                        }
                        if (this.entity == this.boat.getControllingPassenger()) {
                            this.entity.getNavigation().moveTo(this.entity.getOwner(), this.movespeed);
                        }
                    } else if (this.entity.getVehicle() == this.boat) {
                        this.entity.getLookControl().setLookAt(this.entity.getOwner(), 30.0F, 30.0F);
                        this.entity.setPathfindingMalus(PathNodeType.WATER, 1.0F);
                        this.entity.setPathfindingMalus(PathNodeType.WALKABLE, -1F);
                        Path path = this.entity.getNavigation().createPath(this.entity.getOwner(), 3);
                        controlBoat((BoatEntity) this.boat, this.entity, path);
                    } else {
                        this.entity.getNavigation().moveTo(this.boat, this.movespeed);
                        this.entity.getLookControl().setLookAt(this.entity.getOwner(), 30.0F, 30.0F);

                    }
                }
            }
        }
    }

    private void controlBoat(BoatEntity boat, TameableEntity controller, Path path) {
        LivingEntity owner = controller.getOwner();
        if(owner != null && controller == boat.getControllingPassenger()) {
            if(path != null) {

                Vector3d vector3d2 = path.getNextEntityPos(this.entity);
                    if (Math.sqrt(controller.distanceToSqr(vector3d2)) < 5) {
                        if(!path.isDone()) {
                            path.advance();
                        }
                        else{
                            path = controller.getNavigation().createPath(controller.getOwner(), 3);
                        }
                    }

                /*
                if(Math.sqrt(this.boat.getDistanceSq(vector3d2)) < 3 && !path.isFinished()){
                    Vector3d vector3d = this.entity.getPositionVec();
                    double maxDistanceToWaypoint = 3;
                    Vector3i vector3i = path.getNextNodePos();
                    double d0 = Math.abs(this.entity.getPosX() - ((double)vector3i.getX() + (this.entity.getWidth() + 1) / 2D)); //Forge: Fix MC-94054
                    double d2 = Math.abs(this.entity.getPosZ() - ((double)vector3i.getZ() + (this.entity.getWidth() + 1) / 2D)); //Forge: Fix MC-94054
                    boolean flag = d0 < (double)maxDistanceToWaypoint&& d2 < (double)maxDistanceToWaypoint;
                    if (flag || this.entity.canCutCorner(path.getNextNode().nodeType) && this.shouldTargetNextNodeInDirection(vector3d, path) && path.getCurrentPathIndex()<path.getCurrentPathLength()) {
                        path.incrementPathIndex();
                    }
                }

                 */
                if(!(path != null && path.isDone()) && controller.distanceTo(controller.getOwner())>5) {
                    vector3d2 = path.getNextEntityPos(this.entity);
                    BlockPos blockpos = new BlockPos(vector3d2);
                    double d0 = boat.getX() - blockpos.getX();
                    double d1 = boat.getZ() - blockpos.getZ();
                    float f = 0.04F;
                    float oldyaw = boat.yRot;
                    boat.setDeltaMovement(boat.getDeltaMovement().add(MathHelper.sin(-boat.yRot * ((float) Math.PI / 180F)) * f, 0.0D, (double) (MathHelper.cos(boat.yRot * ((float) Math.PI / 180F)) * f)));
                    boat.yRot = (float) (MathHelper.atan2(d1, d0) * (double) (180F / (float) Math.PI)) + 90.0F;
                    float newyaw = boat.yRot;
                    float x1 = (float) Math.sqrt(boat.getDeltaMovement().x()*boat.getDeltaMovement().x()+boat.getDeltaMovement().z()*boat.getDeltaMovement().z());
                    boat.setPaddleState(oldyaw - newyaw < -0 || x1>0.2, oldyaw - newyaw > 0 || x1>0.2);
                }
            }
        }


    }

}

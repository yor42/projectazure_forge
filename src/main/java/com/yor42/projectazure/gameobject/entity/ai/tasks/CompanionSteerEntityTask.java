package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.server.level.ServerLevel;

import static java.lang.Math.abs;

public class CompanionSteerEntityTask extends Behavior<AbstractEntityCompanion> {

    private PathFinder navigator;
    private Path path;

    public CompanionSteerEntityTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, AbstractEntityCompanion entity) {
        boolean isPassenger = entity.isPassenger();
        boolean isControlling = entity.getVehicle()!= null && entity.getVehicle().getControllingPassenger() == entity;
        return isPassenger && isControlling;
    }

    @Override
    protected boolean canStillUse(ServerLevel world, AbstractEntityCompanion entity, long p_212834_3_) {
        boolean isPassenger = entity.isPassenger();
        if(entity.getVehicle()!=null) {
            boolean isControlling = entity.getVehicle().getControllingPassenger() == entity;
            boolean isOwnerStillRidng = entity.getOwner() != null && entity.getOwner().isPassenger();
            return isPassenger && isControlling && isOwnerStillRidng;
        }
        return false;
    }

    @Override
    protected void stop(ServerLevel p_212835_1_, AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        p_212835_2_.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Override
    protected void tick(ServerLevel world, AbstractEntityCompanion entity, long p_212833_3_) {

        if(this.navigator == null){
             this.navigator = new PathFinder(new SwimNodeEvaluator(true), 64);
        }

        Entity vehicle = entity.getVehicle();
        if(vehicle != null){
            if(vehicle instanceof Boat && entity.getOwner()!=null) {
                if(this.path == null) {
                    this.path = this.CreateBoatPath(world, entity, (Boat) vehicle, entity.getOwner().blockPosition());
                }
                controlBoat((Boat) vehicle, entity, this.path);
                entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(entity.getOwner(), true));
            }
        }
    }

    private Path CreateBoatPath(ServerLevel world, AbstractEntityCompanion entity, Boat boat, BlockPos pos){
        if (this.path != null && !this.path.isDone()) {
            return this.path;
        } else {
            world.getProfiler().push("pathfind");
            BlockPos blockpos = boat.blockPosition();
            int i = 64;
            PathNavigationRegion region = new PathNavigationRegion(world, blockpos.offset(-i, -i, -i), blockpos.offset(i, i, i));
            Path path = this.navigator.findPath(region,entity, ImmutableSet.of(pos), 64, 1, 1);
            world.getProfiler().pop();
            return path;
        }
    }

    private void controlBoat(Boat boat, TamableAnimal controller, Path path) {
        LivingEntity owner = controller.getOwner();
        if(owner != null && controller == boat.getControllingPassenger()) {
            if(path != null && !path.isDone()) {

                Vec3 vector3d2 = path.getNextEntityPos(controller);

                if (abs(boat.getX()-vector3d2.x)<3 && abs(boat.getZ()-vector3d2.z)<3) {
                    path.advance();
                }
                if(!path.isDone() && controller.distanceTo(controller.getOwner()) > 5) {
                    vector3d2 = path.getNextEntityPos(controller);
                    BlockPos blockpos = new BlockPos(vector3d2);
                    double d0 = boat.getX() - blockpos.getX();
                    double d1 = boat.getZ() - blockpos.getZ();
                    float f = 0.04F;
                    float oldyaw = boat.getYRot();
                    boat.setYRot((float) (Mth.atan2(d1, d0) * (double) (180F / (float) Math.PI)) + 90.0F);
                    boat.setDeltaMovement(boat.getDeltaMovement().add(Mth.sin(-boat.getYRot() * ((float) Math.PI / 180F)) * f, 0.0D, Mth.cos(boat.getYRot() * ((float) Math.PI / 180F)) * f));
                    float newyaw = boat.getYRot();
                    float x1 = (float) Math.sqrt(boat.getDeltaMovement().x()*boat.getDeltaMovement().x()+boat.getDeltaMovement().z()*boat.getDeltaMovement().z());
                    boat.setPaddleState(oldyaw - newyaw < -0 || x1>0.2, oldyaw - newyaw > 0 || x1>0.2);
                }
            }
            else{
                this.path = null;
            }
        }


    }

}

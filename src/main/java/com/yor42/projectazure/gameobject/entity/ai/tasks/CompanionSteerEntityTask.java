package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.SwimNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Region;
import net.minecraft.world.server.ServerWorld;

import static java.lang.Math.abs;
import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.LOOK_TARGET;

public class CompanionSteerEntityTask extends Task<AbstractEntityCompanion> {

    private PathFinder navigator;
    private Path path;

    public CompanionSteerEntityTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld world, AbstractEntityCompanion entity) {
        boolean isPassenger = entity.isPassenger();
        boolean isControlling = entity.getVehicle()!= null && entity.getVehicle().getControllingPassenger() == entity;
        return isPassenger && isControlling;
    }

    @Override
    protected boolean canStillUse(ServerWorld world, AbstractEntityCompanion entity, long p_212834_3_) {
        boolean isPassenger = entity.isPassenger();
        if(entity.getVehicle()!=null) {
            boolean isControlling = entity.getVehicle().getControllingPassenger() == entity;
            boolean isOwnerStillRidng = entity.getOwner() != null && entity.getOwner().isPassenger();
            return isPassenger && isControlling && isOwnerStillRidng;
        }
        return false;
    }

    @Override
    protected void stop(ServerWorld p_212835_1_, AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        p_212835_2_.getBrain().eraseMemory(LOOK_TARGET);
    }

    @Override
    protected void tick(ServerWorld world, AbstractEntityCompanion entity, long p_212833_3_) {

        if(this.navigator == null){
             this.navigator = new PathFinder(new SwimNodeProcessor(true), 64);
        }

        Entity vehicle = entity.getVehicle();
        if(vehicle != null){
            if(vehicle instanceof BoatEntity && entity.getOwner()!=null) {
                if(this.path == null) {
                    this.path = this.CreateBoatPath(world, entity, (BoatEntity) vehicle, entity.getOwner().blockPosition());
                }
                controlBoat((BoatEntity) vehicle, entity, this.path);
                entity.getBrain().setMemory(LOOK_TARGET, new EntityPosWrapper(entity.getOwner(), true));
            }
        }
    }

    private Path CreateBoatPath(ServerWorld world, AbstractEntityCompanion entity, BoatEntity boat, BlockPos pos){
        if (this.path != null && !this.path.isDone()) {
            return this.path;
        } else {
            world.getProfiler().push("pathfind");
            BlockPos blockpos = boat.blockPosition();
            int i = 64;
            Region region = new Region(world, blockpos.offset(-i, -i, -i), blockpos.offset(i, i, i));
            Path path = this.navigator.findPath(region,entity, ImmutableSet.of(pos), 64, 1, 1);
            world.getProfiler().pop();
            return path;
        }
    }

    private void controlBoat(BoatEntity boat, TameableEntity controller, Path path) {
        LivingEntity owner = controller.getOwner();
        if(owner != null && controller == boat.getControllingPassenger()) {
            if(path != null && !path.isDone()) {

                Vector3d vector3d2 = path.getNextEntityPos(controller);

                if (abs(boat.getX()-vector3d2.x)<3 && abs(boat.getZ()-vector3d2.z)<3) {
                    path.advance();
                }
                if(!path.isDone() && controller.distanceTo(controller.getOwner()) > 5) {
                    vector3d2 = path.getNextEntityPos(controller);
                    BlockPos blockpos = new BlockPos(vector3d2);
                    double d0 = boat.getX() - blockpos.getX();
                    double d1 = boat.getZ() - blockpos.getZ();
                    float f = 0.04F;
                    float oldyaw = boat.yRot;
                    boat.yRot = (float) (MathHelper.atan2(d1, d0) * (double) (180F / (float) Math.PI)) + 90.0F;
                    Vector3d vector = new Vector3d(0, 0,f).yRot(boat.yRot);
                    boat.setDeltaMovement(boat.getDeltaMovement().add(MathHelper.sin(-boat.yRot * ((float) Math.PI / 180F)) * f, 0.0D, (double) (MathHelper.cos(boat.yRot * ((float) Math.PI / 180F)) * f)));
                    float newyaw = boat.yRot;
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

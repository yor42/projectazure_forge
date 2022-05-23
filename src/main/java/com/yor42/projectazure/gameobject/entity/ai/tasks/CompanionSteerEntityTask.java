package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.BoatPathNavigator;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import static java.lang.Math.abs;
import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.LOOK_TARGET;

public class CompanionSteerEntityTask extends Task<AbstractEntityCompanion> {

    private PathNavigator navigator;

    public CompanionSteerEntityTask() {
        super(ImmutableMap.of(MemoryModuleType.RIDE_TARGET, MemoryModuleStatus.VALUE_PRESENT));
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
             this.navigator = new BoatPathNavigator(entity, world);
        }

        entity.getBrain().getMemory(MemoryModuleType.RIDE_TARGET).ifPresent((vehicle)->{
            if(vehicle instanceof BoatEntity && entity.getOwner()!=null) {
                Path path = this.navigator.createPath(entity.getOwner(), 3);
                controlBoat((BoatEntity) vehicle, entity, path, navigator);
                entity.getBrain().setMemory(LOOK_TARGET, new EntityPosWrapper(entity.getOwner(), true));
            }
        });
    }

    private void controlBoat(BoatEntity boat, TameableEntity controller, Path path, PathNavigator navigator) {
        LivingEntity owner = controller.getOwner();
        if(owner != null && controller == boat.getControllingPassenger()) {
            if(path != null) {

                Vector3d vector3d2 = path.getNextEntityPos(controller);
                if (abs(boat.getX()-vector3d2.x)<3 && abs(boat.getZ()-vector3d2.z)<3) {
                    if(!path.isDone()) {
                        path.advance();
                    }
                    else{
                        path = navigator.createPath(controller.getOwner(), 3);
                    }
                }
                if(!(path != null && path.isDone()) && controller.distanceTo(controller.getOwner())>5) {
                    vector3d2 = path.getNextEntityPos(controller);
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

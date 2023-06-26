package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import javax.annotation.Nullable;
import java.util.List;

import static java.lang.Math.abs;

public class CompanionSteerBoatBehavior extends ExtendedBehaviour<AbstractEntityCompanion> {

    private final PathFinder navigator = new PathFinder(new SwimNodeEvaluator(true), 64);
    @Nullable
    private Path path;
    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean shouldKeepRunning(AbstractEntityCompanion entity) {

        LivingEntity owner = entity.getOwner();

        if(owner == null){
            return false;
        }

        if(!(owner.getVehicle() instanceof Boat)){
            return false;
        }
        else if(!(entity.getVehicle() instanceof Boat)){
            return false;
        }


        return entity.getVehicle().getControllingPassenger() == entity;
    }

    @Override
    protected void tick(AbstractEntityCompanion entity) {

        Entity vehicle = entity.getVehicle();
        if(!(vehicle instanceof Boat)){
            return;
        }

        if(entity.getOwner() != null) {
            if(this.path == null) {
                this.path = this.CreateBoatPath((ServerLevel) entity.level, entity, (Boat) vehicle, entity.getOwner().blockPosition());
            }
            controlBoat((Boat) vehicle, entity, this.path);
            entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(entity.getOwner(), true));
        }

        super.tick(entity);
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

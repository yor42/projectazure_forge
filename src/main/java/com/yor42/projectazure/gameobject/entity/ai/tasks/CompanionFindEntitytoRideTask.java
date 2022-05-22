package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class CompanionFindEntitytoRideTask extends Task<AbstractEntityCompanion> {
    private List<Entity> RideableEntityList;

    public CompanionFindEntitytoRideTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.RIDE_TARGET, MemoryModuleStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerWorld world, AbstractEntityCompanion entity) {
        LivingEntity owner = entity.getOwner();
        if(entity.isCriticallyInjured()){
            return false;
        }
        if(owner == null){
            return false;
        }

        if(!owner.isPassenger()) {
            return false;
        }

        if(entity.isPassenger()){
            if(!owner.isPassenger()) {
                return false;
            }
        }

        if(owner instanceof PlayerEntity && owner.getVehicle() != null){
            Entity vehicle = owner.getVehicle();
            Class<? extends Entity> cls = vehicle.getClass();
            if(vehicle instanceof BoatEntity && entity instanceof EntityKansenBase) {
                if (entity.canUseRigging()) {
                    ItemStack rigging = entity.getRigging();
                    boolean canSail = rigging.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map((fluidtank) -> {
                        int amount = fluidtank.getFluidInTank(0).getAmount();
                        return amount > 0;
                    }).orElse(false);
                    if (canSail) {
                        return false;
                    }
                }
            }
            List<Entity> entitylist = entity.getCommandSenderWorld().getEntities(entity, entity.getBoundingBox().expandTowards(new Vector3d(10, 5, 10)), (candidate)->{
                if(candidate.getClass() != cls){
                    return false;
                }
                else if(cls == BoatEntity.class){
                    Entity controller = candidate.getControllingPassenger();
                    if(candidate.getPassengers().size() == 2){
                        return false;
                    }
                    return candidate.getPassengers().size()<2 && controller instanceof AbstractEntityCompanion && ((AbstractEntityCompanion) controller).isOwnedBy(entity.getOwner()) || candidate.getPassengers().isEmpty();
                }
                else if(cls == HorseEntity.class){
                    return candidate.getPassengers().isEmpty();
                }
                return false;
            });
            if(entity.getVehicle()!= null && entity.getVehicle().getClass() ==cls){
                return true;
            }
            else if(vehicle instanceof BoatEntity && vehicle.getPassengers().size() < 2){
                return true;
            }
            else if(!entitylist.isEmpty()){
                this.RideableEntityList = entitylist;
                return true;
            }
        }
        return false;
    }

    @Override
    protected void stop(ServerWorld p_212835_1_, AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        this.RideableEntityList = new ArrayList<>();
    }

    @Override
    protected void start(@Nonnull ServerWorld world, AbstractEntityCompanion entity, long p_212831_3_) {
        LivingEntity owner = entity.getOwner();
        if(owner instanceof PlayerEntity && owner.getVehicle() != null) {
            Entity vehicle = owner.getVehicle();
            if(entity.getVehicle() != null && entity.getVehicle().getClass() == vehicle.getClass()) {
                entity.getBrain().setMemory(MemoryModuleType.RIDE_TARGET, vehicle);
            }
            else if(vehicle instanceof BoatEntity && vehicle.getPassengers().size()<2){
                entity.getBrain().setMemory(MemoryModuleType.RIDE_TARGET, vehicle);
                return;
            }
            else if(!this.RideableEntityList.isEmpty()){
                entity.getBrain().setMemory(MemoryModuleType.RIDE_TARGET, this.RideableEntityList.get(0));
            }
        }
    }
}

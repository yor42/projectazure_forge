package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CompanionFindEntitytoRideTask extends Behavior<AbstractEntityCompanion> {
    private List<Entity> RideableEntityList;

    public CompanionFindEntitytoRideTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.RIDE_TARGET, MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel world, AbstractEntityCompanion entity) {
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

        if(owner instanceof Player && owner.getVehicle() != null){
            Entity vehicle = owner.getVehicle();
            Class<? extends Entity> cls = vehicle.getClass();
            if(vehicle instanceof Boat && entity instanceof EntityKansenBase) {
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
            List<Entity> entitylist = entity.getLevel().getEntities(entity, entity.getBoundingBox().expandTowards(new Vec3(10, 5, 10)), (candidate)->{
                if(candidate.getClass() != cls){
                    return false;
                }
                else if(cls == Boat.class){
                    Entity controller = candidate.getControllingPassenger();
                    if(candidate.getPassengers().size() == 2){
                        return false;
                    }
                    return (candidate.getPassengers().size()<2 && controller instanceof AbstractEntityCompanion && ((AbstractEntityCompanion) controller).isOwnedBy(owner)) || candidate.getPassengers().isEmpty() || candidate.getControllingPassenger() == owner;
                }
                else if(cls == Horse.class){
                    return candidate.getPassengers().isEmpty();
                }
                return false;
            });
            if(entity.getVehicle()!= null && entity.getVehicle().getClass() ==cls){
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
    protected void stop(ServerLevel p_212835_1_, AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        this.RideableEntityList = new ArrayList<>();
    }

    @Override
    protected void start(@Nonnull ServerLevel world, AbstractEntityCompanion entity, long p_212831_3_) {
        LivingEntity owner = entity.getOwner();
        if(owner instanceof Player && owner.getVehicle() != null) {
            Entity vehicle = owner.getVehicle();
            if(entity.getVehicle() != null && entity.getVehicle().getClass() == vehicle.getClass()) {
                entity.getBrain().setMemory(MemoryModuleType.RIDE_TARGET, vehicle);
            }
            else if(vehicle instanceof Boat && vehicle.getPassengers().size()<2){
                entity.getBrain().setMemory(MemoryModuleType.RIDE_TARGET, vehicle);
            }
            else if(!this.RideableEntityList.isEmpty()){
                entity.getBrain().setMemory(MemoryModuleType.RIDE_TARGET, this.RideableEntityList.get(0));
            }
        }
    }
}

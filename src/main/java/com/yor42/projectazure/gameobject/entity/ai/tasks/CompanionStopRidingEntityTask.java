package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.function.BiPredicate;

public class CompanionStopRidingEntityTask extends Task<AbstractEntityCompanion> {

    private final BiPredicate<AbstractEntityCompanion, Entity> dontRideIf = (entity, vehicle)-> {
        if (entity.getOwner() != null) {
            LivingEntity owner = entity.getOwner();
            if (vehicle instanceof BoatEntity) {
                return !owner.isInWater() && !owner.isPassenger();
            }
            else{
                return !owner.isPassenger();
            }
        } else {
            return true;
        }
    };

    public CompanionStopRidingEntityTask() {
        super(ImmutableMap.of(MemoryModuleType.RIDE_TARGET, MemoryModuleStatus.VALUE_PRESENT));
    }

    protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, AbstractEntityCompanion p_212832_2_) {
        Entity entity = p_212832_2_.getVehicle();
        Entity entity1 = p_212832_2_.getBrain().getMemory(MemoryModuleType.RIDE_TARGET).orElse((Entity)null);
        if (entity == null && entity1 == null) {
            return false;
        } else {
            Entity entity2 = entity == null ? entity1 : entity;
            return !this.isVehicleValid(p_212832_2_, entity2) || this.dontRideIf.test(p_212832_2_, entity2);
        }
    }

    private boolean isVehicleValid(AbstractEntityCompanion p_233892_1_, Entity vehicle) {
        return vehicle instanceof BoatEntity || vehicle.isAlive() && vehicle.level == p_233892_1_.level;
    }

    protected void start(ServerWorld p_212831_1_, AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        p_212831_2_.stopRiding();
        p_212831_2_.getBrain().eraseMemory(MemoryModuleType.RIDE_TARGET);
    }
}

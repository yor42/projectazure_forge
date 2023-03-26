package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.vehicle.Boat;

import java.util.function.BiPredicate;

public class CompanionStopRidingEntityTask extends Behavior<AbstractEntityCompanion> {

    private final BiPredicate<AbstractEntityCompanion, Entity> dontRideIf = (entity, vehicle)-> {
        if (entity.getOwner() != null) {
            LivingEntity owner = entity.getOwner();
            if (vehicle instanceof Boat) {
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
        super(ImmutableMap.of(MemoryModuleType.RIDE_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, AbstractEntityCompanion p_212832_2_) {
        Entity entity = p_212832_2_.getVehicle();
        Entity entity1 = p_212832_2_.getBrain().getMemory(MemoryModuleType.RIDE_TARGET).orElse(null);
        if (entity == null && entity1 == null) {
            return false;
        } else {
            Entity entity2 = entity == null ? entity1 : entity;
            return !this.isVehicleValid(p_212832_2_, entity2) || this.dontRideIf.test(p_212832_2_, entity2);
        }
    }

    private boolean isVehicleValid(AbstractEntityCompanion p_233892_1_, Entity vehicle) {
        return vehicle instanceof Boat || vehicle.isAlive() && vehicle.level == p_233892_1_.level;
    }

    protected void start(ServerLevel p_212831_1_, AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        p_212831_2_.stopRiding();
        p_212831_2_.getBrain().eraseMemory(MemoryModuleType.RIDE_TARGET);
    }
}

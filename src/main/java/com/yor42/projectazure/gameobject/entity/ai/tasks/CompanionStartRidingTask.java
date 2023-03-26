package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.server.level.ServerLevel;

import java.util.concurrent.atomic.AtomicBoolean;

import static net.minecraft.world.entity.ai.memory.MemoryModuleType.RIDE_TARGET;

public class CompanionStartRidingTask extends Behavior<AbstractEntityCompanion> {
    public CompanionStartRidingTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, RIDE_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, AbstractEntityCompanion p_212832_2_) {
        if(p_212832_2_.isCriticallyInjured()){
            return false;
        }

        return !p_212832_2_.isPassenger();
    }

    protected void start(ServerLevel p_212831_1_, AbstractEntityCompanion entity, long p_212831_3_) {
        AtomicBoolean cancelled = new AtomicBoolean(false);
        entity.getBrain().getMemory(RIDE_TARGET).ifPresent((target) -> {
            if(target instanceof Boat && target.getPassengers().size()==2 || !(target instanceof Boat) && !target.getPassengers().isEmpty()){
                entity.getBrain().eraseMemory(RIDE_TARGET);
                cancelled.set(true);
            }
        });
        if(!cancelled.get()) {
            if (this.isCloseEnoughToStartRiding(entity)) {
                boolean success = entity.startRiding(this.getRidableEntity(entity));
                if (!success) {
                    entity.getBrain().eraseMemory(RIDE_TARGET);
                }
            } else {
                BehaviorUtils.setWalkAndLookTargetMemories(entity, this.getRidableEntity(entity), 1, 1);
            }
        }
    }

    private boolean isCloseEnoughToStartRiding(AbstractEntityCompanion p_233925_1_) {
        return this.getRidableEntity(p_233925_1_).closerThan(p_233925_1_, 9.0D);
    }

    private Entity getRidableEntity(AbstractEntityCompanion p_233926_1_) {
        return p_233926_1_.getBrain().getMemory(RIDE_TARGET).get();
    }

}

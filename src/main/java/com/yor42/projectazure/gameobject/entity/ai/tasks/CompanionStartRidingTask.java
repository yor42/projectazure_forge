package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.concurrent.atomic.AtomicBoolean;

import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.RIDE_TARGET;

public class CompanionStartRidingTask extends Task<AbstractEntityCompanion> {
    public CompanionStartRidingTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT, RIDE_TARGET, MemoryModuleStatus.VALUE_PRESENT));
    }

    protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, AbstractEntityCompanion p_212832_2_) {
        if(p_212832_2_.isCriticallyInjured()){
            return false;
        }

        return !p_212832_2_.isPassenger();
    }

    protected void start(ServerWorld p_212831_1_, AbstractEntityCompanion entity, long p_212831_3_) {
        AtomicBoolean cancelled = new AtomicBoolean(false);
        entity.getBrain().getMemory(RIDE_TARGET).ifPresent((target) -> {
            if(target instanceof BoatEntity && target.getPassengers().size()==2 || !(target instanceof BoatEntity) && !target.getPassengers().isEmpty()){
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
                BrainUtil.setWalkAndLookTargetMemories(entity, this.getRidableEntity(entity), 1, 1);
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

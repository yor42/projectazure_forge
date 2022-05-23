package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

public class CompanionSprintTask extends Task<AbstractEntityCompanion> {

    public CompanionSprintTask() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, AbstractEntityCompanion p_212832_2_) {
        return p_212832_2_.getFoodStats().getFoodLevel()>=10;
    }

    @Override
    protected void stop(ServerWorld p_212835_1_, AbstractEntityCompanion entity, long p_212835_3_) {

    }

    @Override
    protected void start(ServerWorld world, AbstractEntityCompanion entity, long p_212831_3_) {
        entity.getBrain().getMemory(MemoryModuleType.WALK_TARGET).ifPresent(((pos)->{
            boolean shouldSprint = Math.sqrt(entity.distanceToSqr(pos.getTarget().currentPosition())) >= pos.getCloseEnoughDist() + 4;
            entity.setSprinting(shouldSprint);
        }));
        if(!entity.getBrain().getMemory(MemoryModuleType.WALK_TARGET).isPresent()){
            entity.setSprinting(false);
        }
    }
}

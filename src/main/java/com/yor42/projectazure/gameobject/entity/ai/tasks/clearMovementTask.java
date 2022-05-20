package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

public class clearMovementTask extends Task<AbstractEntityCompanion> {
    public clearMovementTask() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected void start(ServerWorld p_212831_1_, AbstractEntityCompanion entity, long p_212831_3_) {
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }
}

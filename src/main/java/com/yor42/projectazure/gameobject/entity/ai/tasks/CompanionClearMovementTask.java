package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.server.level.ServerLevel;

public class CompanionClearMovementTask extends Behavior<AbstractEntityCompanion> {
    public CompanionClearMovementTask() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected void start(ServerLevel p_212831_1_, AbstractEntityCompanion entity, long p_212831_3_) {
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }
}

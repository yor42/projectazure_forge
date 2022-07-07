package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;

import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.ATTACK_TARGET;

public class CompanionEndAttackTask extends Task<AbstractEntityCompanion> {
    public CompanionEndAttackTask() {
        super(ImmutableMap.of(ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED));
    }
    protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, AbstractEntityCompanion p_212832_2_) {
        return p_212832_2_.getBrain().getMemory(ATTACK_TARGET).map(LivingEntity::isDeadOrDying).orElse(false);
    }

    protected void start(ServerWorld p_212831_1_, AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        p_212831_2_.getBrain().getMemory(ATTACK_TARGET).ifPresent((livingentity)->{
            p_212831_2_.getBrain().eraseMemory(ATTACK_TARGET);
            p_212831_2_.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        });
    }

}

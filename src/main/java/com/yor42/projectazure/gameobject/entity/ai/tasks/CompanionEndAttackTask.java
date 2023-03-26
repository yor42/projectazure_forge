package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import static net.minecraft.world.entity.ai.memory.MemoryModuleType.ATTACK_TARGET;

public class CompanionEndAttackTask extends Behavior<AbstractEntityCompanion>{
    public CompanionEndAttackTask() {
        super(ImmutableMap.of(ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
    }
    protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, AbstractEntityCompanion p_212832_2_) {
        return p_212832_2_.getBrain().getMemory(ATTACK_TARGET).map(LivingEntity::isDeadOrDying).orElse(false);
    }

    protected void start(ServerLevel p_212831_1_, AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        p_212831_2_.getBrain().getMemory(ATTACK_TARGET).ifPresent((livingentity)->{
            p_212831_2_.getBrain().eraseMemory(ATTACK_TARGET);
            p_212831_2_.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        });
    }

}

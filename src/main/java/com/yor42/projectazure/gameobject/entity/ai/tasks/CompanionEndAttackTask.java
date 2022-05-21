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

import java.util.Map;

public class CompanionEndAttackTask extends Task<AbstractEntityCompanion> {
    public CompanionEndAttackTask() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED));
    }
    protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, AbstractEntityCompanion p_212832_2_) {
        return this.getAttackTarget(p_212832_2_).isDeadOrDying();
    }

    protected void start(ServerWorld p_212831_1_, AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        LivingEntity livingentity = this.getAttackTarget(p_212831_2_);

        if (livingentity.getType() != EntityType.PLAYER || p_212831_1_.getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)) {
            p_212831_2_.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
            p_212831_2_.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        }

    }

    private LivingEntity getAttackTarget(AbstractEntityCompanion p_233980_1_) {
        return p_233980_1_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

}

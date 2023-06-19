package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import static net.minecraft.world.entity.ai.memory.MemoryModuleType.ATTACK_TARGET;

public class StartAttackingHostileTask extends Behavior<AbstractEntityCompanion> {
    public StartAttackingHostileTask() {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_HOSTILE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, AbstractEntityCompanion pOwner) {
        return !pOwner.isCriticallyInjured() && pOwner.wantsToAttack(pOwner.getBrain().getMemory(MemoryModuleType.NEAREST_HOSTILE).get(), pOwner);
    }

    @Override
    protected void start(ServerLevel pLevel, AbstractEntityCompanion pEntity, long pGameTime) {
        LivingEntity tgt = pEntity.getBrain().getMemory(MemoryModuleType.NEAREST_HOSTILE).get();
        Brain<AbstractEntityCompanion> brain = pEntity.getBrain();
        brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        brain.setMemoryWithExpiry(ATTACK_TARGET, tgt, 1000L);
        pEntity.setAggressive(true);
    }
}

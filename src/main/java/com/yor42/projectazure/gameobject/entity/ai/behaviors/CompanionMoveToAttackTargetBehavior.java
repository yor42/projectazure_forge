package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.Objects;

public class CompanionMoveToAttackTargetBehavior extends ExtendedBehaviour<AbstractEntityCompanion> {

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {
        BehaviorUtils.setWalkAndLookTargetMemories(entity, Objects.requireNonNull(BrainUtils.getTargetOfEntity(entity)), 1,0);
    }
}

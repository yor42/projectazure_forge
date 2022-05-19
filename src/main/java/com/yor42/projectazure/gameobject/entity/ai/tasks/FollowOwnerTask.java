package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class FollowOwnerTask extends Task<AbstractEntityCompanion> {
    public FollowOwnerTask() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerWorld world, AbstractEntityCompanion entity) {
        LivingEntity owner = entity.getOwner();
        return owner!=null && entity.distanceTo(owner)>=4;
    }

    protected void start(@Nonnull ServerWorld p_212831_1_, @Nonnull AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        if (p_212831_2_.getOwner()!=null) {
            BrainUtil.setWalkAndLookTargetMemories(p_212831_2_, p_212831_2_.getOwner(), 1, 4);
        }
    }
}

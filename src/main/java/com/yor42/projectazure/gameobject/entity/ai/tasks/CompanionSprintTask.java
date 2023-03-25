package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.mixin.PathNavigatorAccessors;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;

public class CompanionSprintTask extends Behavior<AbstractEntityCompanion> {

    public CompanionSprintTask() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, AbstractEntityCompanion p_212832_2_) {
        return p_212832_2_.getFoodStats().getFoodLevel()>=10 && ((PathNavigatorAccessors)p_212832_2_.getNavigation()).getSpeedModifier()>=1;
    }

    @Override
    protected void stop(ServerLevel p_212835_1_, AbstractEntityCompanion entity, long p_212835_3_) {

    }

    @Override
    protected void start(ServerLevel world, AbstractEntityCompanion entity, long p_212831_3_) {
        entity.getBrain().getMemory(MemoryModuleType.WALK_TARGET).ifPresent(((pos)->{
            BlockPos position = pos.getTarget().currentBlockPosition();
            BlockPos entityPosition = entity.blockPosition();
            boolean shouldSprint = position.closerThan(new Vec3i(entityPosition.getX(), position.getY(), entityPosition.getZ()), Math.max(pos.getCloseEnoughDist() + 4, 8));
            entity.setSprinting(shouldSprint);
        }));
        if(!entity.getBrain().getMemory(MemoryModuleType.WALK_TARGET).isPresent()){
            entity.setSprinting(false);
        }
    }
}

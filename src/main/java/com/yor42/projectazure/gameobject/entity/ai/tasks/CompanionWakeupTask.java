package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.WakeUpTask;
import net.minecraft.world.server.ServerWorld;

public class CompanionWakeupTask extends WakeUpTask {
    @Override
    protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, LivingEntity entity) {

        if(entity instanceof AbstractEntityCompanion){
            if(((AbstractEntityCompanion) entity).isCriticallyInjured()){
                return false;
            }
        }

        return super.checkExtraStartConditions(p_212832_1_, entity);
    }
}

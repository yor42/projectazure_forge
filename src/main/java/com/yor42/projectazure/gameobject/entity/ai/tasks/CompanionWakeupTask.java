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

    @Override
    protected void start(ServerWorld p_212831_1_, LivingEntity p_212831_2_, long p_212831_3_) {
        if(p_212831_2_ instanceof AbstractEntityCompanion) {
            if (((AbstractEntityCompanion) p_212831_2_).shouldBeSitting) {
                ((AbstractEntityCompanion) p_212831_2_).setOrderedToSit(true);
            }
            super.start(p_212831_1_, p_212831_2_, p_212831_3_);
        }
    }
}

package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.WakeUp;

public class CompanionWakeupTask extends WakeUp {
    @Override
    protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, LivingEntity entity) {

        if(entity instanceof AbstractEntityCompanion){
            if(((AbstractEntityCompanion) entity).isCriticallyInjured()){
                return false;
            }
        }

        return super.checkExtraStartConditions(p_212832_1_, entity);
    }

    @Override
    protected void start(ServerLevel p_212831_1_, LivingEntity p_212831_2_, long p_212831_3_) {
        if(p_212831_2_ instanceof AbstractEntityCompanion) {
            AbstractEntityCompanion entity = (AbstractEntityCompanion) p_212831_2_;
            if (entity.shouldBeSitting) {
                entity.setOrderedToSit(true);
            }
        }
    }
}

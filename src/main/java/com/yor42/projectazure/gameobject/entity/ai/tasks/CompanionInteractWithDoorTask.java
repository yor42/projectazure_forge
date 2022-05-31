package com.yor42.projectazure.gameobject.entity.ai.tasks;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.InteractWithDoorTask;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;

public class CompanionInteractWithDoorTask extends InteractWithDoorTask {

    @Override
    protected void start(ServerWorld p_212831_1_, LivingEntity p_212831_2_, long p_212831_3_) {
        super.start(p_212831_1_, p_212831_2_, p_212831_3_);
        p_212831_2_.swing(Hand.MAIN_HAND);
    }
}

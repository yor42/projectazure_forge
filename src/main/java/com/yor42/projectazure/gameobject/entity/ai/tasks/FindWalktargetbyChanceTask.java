package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.brain.task.FindWalkTargetTask;
import net.minecraft.world.server.ServerWorld;

public class FindWalktargetbyChanceTask extends FindWalkTargetTask {

    private final float chance;
    int tickcount = 0;

    public FindWalktargetbyChanceTask(float p_i50336_1_, int xdist, int ydist , float chance) {
        super(p_i50336_1_, xdist, ydist);
        this.chance = chance;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, CreatureEntity p_212832_2_) {
        if (this.tickcount >= 100) {
            this.tickcount = 0;
            return MathUtil.getRand().nextFloat() <= this.chance;
        }
        this.tickcount++;
        return false;
    }
}

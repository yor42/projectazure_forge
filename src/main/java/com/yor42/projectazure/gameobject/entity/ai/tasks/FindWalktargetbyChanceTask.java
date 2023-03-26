package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.VillageBoundRandomStroll;
import net.minecraft.server.level.ServerLevel;

public class FindWalktargetbyChanceTask extends VillageBoundRandomStroll {

    private final float chance;
    int tickcount = 0;

    public FindWalktargetbyChanceTask(float p_i50336_1_, int xdist, int ydist , float chance) {
        super(p_i50336_1_, xdist, ydist);
        this.chance = chance;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, PathfinderMob p_212832_2_) {
        if (this.tickcount >= 100) {
            this.tickcount = 0;
            return MathUtil.getRand().nextFloat() <= this.chance;
        }
        this.tickcount++;
        return false;
    }
}

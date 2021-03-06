package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.ai.goal.Goal;

public class CompanionSleepAtHomeGoal extends Goal {

    private AbstractEntityCompanion companion;

    public CompanionSleepAtHomeGoal(AbstractEntityCompanion companion){
        this.companion = companion;
    }

                                    @Override
    public boolean shouldExecute() {
        return false;
    }
}

package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class KansenSitGoal extends Goal{

    private EntityKansenBase entity;
    private LivingEntity Owner;

    public KansenSitGoal(EntityKansenBase entity){
        this.entity = entity;
        this.setMutexFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        return entity.isSitting();
    }

    @Override
    public void startExecuting() {
        this.entity.func_233687_w_(true);
        this.entity.setJumping(false);
    }

    @Override
    public void tick() {
        this.entity.getNavigator().clearPath();
        this.entity.setAttackTarget(null);
    }
}

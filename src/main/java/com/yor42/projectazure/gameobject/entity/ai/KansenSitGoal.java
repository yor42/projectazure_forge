package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.SitGoal;

import java.util.EnumSet;

public class KansenSitGoal extends SitGoal {

    private AbstractEntityCompanion entity;
    private LivingEntity Owner;

    public KansenSitGoal(AbstractEntityCompanion entity){
        super(entity);
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

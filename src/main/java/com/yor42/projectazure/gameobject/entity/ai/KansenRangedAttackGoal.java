package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.IShipRangedAttack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

public class KansenRangedAttackGoal extends Goal {

    IShipRangedAttack host;
    private LivingEntity attackTarget;
    private final double entityMoveSpeed;
    private int seeTime;
    private final int maxRangedAttackTime;
    private final float attackRadius;
    private final float maxAttackDistance;

    public KansenRangedAttackGoal(IShipRangedAttack entity, double movespeed, int maxAttackTime, float maxAttackDistanceIn){
        if (!(entity instanceof LivingEntity)) {
            throw new IllegalArgumentException("KansenRangedAttackGoal used here doesn't extend living entity. well what do you want me to do, dead bush shooting cannon and torpedo?");
        }
        this.host = entity;
        this.entityMoveSpeed = movespeed;
        this.attackRadius = maxAttackDistanceIn;
        this.maxAttackDistance = maxAttackDistanceIn*maxAttackDistanceIn;
        this.maxRangedAttackTime = maxAttackTime;

    }

    @Override
    public boolean shouldExecute() {
        return false;
    }
}

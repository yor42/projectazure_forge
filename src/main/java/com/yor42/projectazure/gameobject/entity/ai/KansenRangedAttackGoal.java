package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.IShipRangedAttack;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;

public class KansenRangedAttackGoal extends Goal {

    private IShipRangedAttack host;
    private MobEntity entityHost;
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
        this.entityHost = (MobEntity) entity;
        this.entityMoveSpeed = movespeed;
        this.attackRadius = maxAttackDistanceIn;
        this.maxAttackDistance = maxAttackDistanceIn*maxAttackDistanceIn;
        this.maxRangedAttackTime = maxAttackTime;

    }

    @Override
    public boolean shouldExecute() {
        LivingEntity target = this.entityHost.getAttackTarget();
        if(target != null && target.isAlive()){
            this.attackTarget = target;
            return true;
        }
        else return false;
    }

    public boolean shouldContinueExecuting() {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }

    @Override
    public void resetTask() {
        this.attackTarget = null;
    }

    @Override
    public void tick() {
        double distance = this.entityHost.getDistanceSq(this.attackTarget.getPosX(), this.attackTarget.getPosY(), this.attackTarget.getPosZ());
        boolean canSee = this.entityHost.getEntitySenses().canSee(this.attackTarget);
        if (canSee) {
            ++this.seeTime;
        } else {
            this.seeTime = 0;
        }

        if (!(distance > (double)this.maxAttackDistance) && this.seeTime >= 5) {
            this.entityHost.getNavigator().clearPath();
        } else {
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }

        this.entityHost.getLookController().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);

        float f = MathHelper.sqrt(distance) / this.attackRadius;
        float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
        this.host.AttackUsingCannon(this.attackTarget, lvt_5_1_);
    }
}

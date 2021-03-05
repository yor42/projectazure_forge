package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.hasGunOrTorpedo;
import static com.yor42.projectazure.libs.utils.MathUtil.getRand;

public class KansenRangedAttackGoal extends Goal {

    private EntityKansenBase entityHost;
    private LivingEntity attackTarget;
    private final double entityMoveSpeed;
    private int seeTime;
    private int AttackIntervalMax;
    private int CannonAttackDelay;
    private final float CannonattackRadius, TorpedoAttackRadius;
    private final float maxCannonAttackDistance;
    private final float maxTorpedoAttackDistance;
    private int minAttackInterval;
    int torpedoAttackDelay;

    public KansenRangedAttackGoal(EntityKansenBase entity, double movespeed, int MinAttackInterval , int MaxAttackInterval, float maxCannonAttackDistanceIn, float MaxTorpedoAttackDistance){
        if (!(entity instanceof LivingEntity)) {
            throw new IllegalArgumentException("KansenRangedAttackGoal used here doesn't extend living entity. well what do you want me to do, dead bush shooting cannon and torpedo?");
        }
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.entityHost = entity;
        this.entityMoveSpeed = movespeed;
        this.maxCannonAttackDistance = maxCannonAttackDistanceIn;
        this.CannonattackRadius = maxCannonAttackDistanceIn*maxCannonAttackDistanceIn;
        this.AttackIntervalMax = MaxAttackInterval;
        this.minAttackInterval = MinAttackInterval;
        this.maxTorpedoAttackDistance = MaxTorpedoAttackDistance;
        this.TorpedoAttackRadius = MaxTorpedoAttackDistance*MaxTorpedoAttackDistance;
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity target = this.entityHost.getAttackTarget();

        if(this.entityHost instanceof EntityKansenBase) {
            if (target != null && target.isAlive() && ((EntityKansenBase) this.entityHost).hasRigging()) {
                if(hasGunOrTorpedo(((EntityKansenBase) this.entityHost).getRigging())) {
                    this.attackTarget = target;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean shouldContinueExecuting() {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }

    @Override
    public void resetTask() {
        this.attackTarget = null;
        this.CannonAttackDelay = -1;
        this.torpedoAttackDelay = -1;
        this.entityHost.setAggroed(false);
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.entityHost.setAggroed(true);
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

        if (distance <= (double)this.maxCannonAttackDistance && this.seeTime >= 5) {
            this.entityHost.getNavigator().clearPath();
        } else {
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }

        this.entityHost.getLookController().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);

        float f = MathHelper.sqrt(distance) / this.CannonattackRadius;

        if(--this.CannonAttackDelay == 0 && canSee && distance<=this.maxCannonAttackDistance) {
            float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
            this.entityHost.AttackUsingCannon(this.attackTarget, lvt_5_1_);
            this.CannonAttackDelay = MathHelper.floor(f * (float) (this.AttackIntervalMax - this.minAttackInterval) + (float) this.minAttackInterval);
        }
        else if(this.CannonAttackDelay <0){
            this.CannonAttackDelay = MathHelper.floor(f * (float) (this.AttackIntervalMax - this.minAttackInterval) + (float) this.minAttackInterval);
        }

        float f2 = MathHelper.sqrt(distance) / this.TorpedoAttackRadius;

        if(--this.torpedoAttackDelay == 0 && canSee && distance<=this.maxTorpedoAttackDistance) {
            float lvt_5_1_ = MathHelper.clamp(f2, 0.1F, 1.0F);
            this.entityHost.AttackUsingTorpedo(this.attackTarget, lvt_5_1_);
            this.torpedoAttackDelay = (int) (300+(getRand().nextFloat()*140));
        }
        else if(this.torpedoAttackDelay <0){
            this.torpedoAttackDelay = (int) (20+(getRand().nextFloat()*10));
        }

    }
}

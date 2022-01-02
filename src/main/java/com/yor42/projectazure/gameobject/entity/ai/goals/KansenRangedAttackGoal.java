package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import net.minecraft.entity.LivingEntity;
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
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    private int minAttackInterval;
    int torpedoAttackDelay;

    public KansenRangedAttackGoal(EntityKansenBase entity, double movespeed, int MinAttackInterval , int MaxAttackInterval, float maxCannonAttackDistanceIn, float MaxTorpedoAttackDistance){
        if (entity == null) {
            throw new IllegalArgumentException("entity of this goal is null. We can't shoot guns and torpedo from thin air mate.");
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

        if(this.entityHost != null) {
            if (target != null && target.isAlive() && this.entityHost.hasRigging()) {
                boolean isArmed = hasGunOrTorpedo(this.entityHost.getRigging());
                boolean isSailing = this.entityHost.isSailing() || PAConfig.CONFIG.EnableShipLandCombat.get();
                if(isArmed && isSailing) {
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
        this.entityHost.getNavigator().clearPath();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.entityHost.setAggroed(true);
        this.entityHost.getNavigator().clearPath();
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
            ++this.strafingTime;
        } else {
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
            this.strafingTime = -1;
        }
        this.entityHost.faceEntity(attackTarget, 30.0F, 30.0F);
        if (this.strafingTime >= 20) {
            if ((double)this.entityHost.getRNG().nextFloat() < 0.3D) {
                this.strafingClockwise = !this.strafingClockwise;
            }

            if ((double)this.entityHost.getRNG().nextFloat() < 0.3D) {
                this.strafingBackwards = !this.strafingBackwards;
            }

            this.strafingTime = 0;
        }

        if (this.strafingTime > -1) {
            if (distance > (double)(this.maxCannonAttackDistance * 0.75F)) {
                this.strafingBackwards = false;
            } else if (distance < (double)(this.maxCannonAttackDistance * 0.25F)) {
                this.strafingBackwards = true;
            }

            //this.entityHost.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
            this.entityHost.faceEntity(attackTarget, 30.0F, 30.0F);
        }
        else {
            this.entityHost.getLookController().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
        }
        float f = MathHelper.sqrt(distance) / this.CannonattackRadius;

        if(--this.CannonAttackDelay == 0 && canSee && distance<=this.maxCannonAttackDistance) {
            this.entityHost.AttackUsingCannon(this.attackTarget);
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

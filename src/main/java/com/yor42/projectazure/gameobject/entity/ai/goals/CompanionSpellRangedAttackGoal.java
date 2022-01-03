package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.magicuser.AbstractCompanionMagicUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

public class CompanionSpellRangedAttackGoal extends Goal {
    @Nonnull
    private final AbstractCompanionMagicUser host;
    @Nullable private LivingEntity target;
    private final float maxAttackDistance;
    private int seeTime;
    private int AttackIntervalMax;
    private int CannonAttackDelay;
    int torpedoAttackDelay;

    public CompanionSpellRangedAttackGoal(@Nonnull AbstractCompanionMagicUser companion, float maxDistance){
        this.host = companion;
        this.maxAttackDistance = maxDistance;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        @Nullable
        LivingEntity targetEntity = this.host.getAttackTarget();
        if(targetEntity == null || !targetEntity.isAlive()){
            return false;
        }


        if(this.host.shouldUseSpell()) {
            this.target = targetEntity;
            return true;
        }
        return false;
    }

    public boolean shouldContinueExecuting() {
        return this.shouldExecute() || !this.host.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.host.getNavigator().clearPath();
        this.host.setAggroed(true);
    }

    @Override
    public void resetTask() {
        this.target = null;
        this.host.getNavigator().clearPath();
        this.host.setAggroed(false);
    }

    @Override
    public void tick() {
        if(this.target != null) {
            double distance = this.host.getDistance(this.target);
            boolean canSee = this.host.getEntitySenses().canSee(this.target);

            boolean isTooclose = distance <=this.maxAttackDistance*0.5;
            boolean isTooFar = distance > (double)this.maxAttackDistance;

            if (canSee) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }
            this.host.faceEntity(this.target, 30.0F, 30.0F);
            if(!this.host.isUsingSpell()) {
                if (isTooFar) {
                    this.host.setSprinting(distance >= this.maxAttackDistance*1.5);
                    this.host.getNavigator().tryMoveToEntityLiving(this.target, 1);
                } else if (isTooclose) {
                    this.host.getMoveHelper().strafe(-0.5F, 0);
                    this.host.setSprinting(false);
                } else if(this.seeTime >= 5){
                    this.host.getNavigator().clearPath();
                    this.host.setSprinting(false);
                    this.host.StartShootingEntity(this.target);
                }else {
                    this.host.setSprinting(distance >= this.maxAttackDistance*1.5);
                    this.host.getNavigator().tryMoveToEntityLiving(this.target, 1);
                }
            }

        }
    }
}

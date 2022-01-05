package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.magicuser.AbstractCompanionMagicUser;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.AbstractSwordUserBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

public class CompanionSwordUserMeleeAttack extends Goal {

    /*
    Yea I Shamelessly reused Spell goal code for this.
     */

    @Nonnull
    private final AbstractSwordUserBase host;
    @Nullable private LivingEntity target;
    private int seeTime;

    public CompanionSwordUserMeleeAttack(@Nonnull AbstractSwordUserBase companion){
        this.host = companion;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        @Nullable
        LivingEntity targetEntity = this.host.getAttackTarget();
        if(targetEntity == null || !targetEntity.isAlive()){
            return false;
        }

        boolean shouldAttack = this.host.shouldAttack();
        if(shouldAttack) {
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

            boolean isTooFar = distance > 3;

            if (canSee) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }
            this.host.faceEntity(this.target, 30.0F, 30.0F);
            if(!this.host.isAttacking()) {
                if (isTooFar) {
                    this.host.setSprinting(distance >= 8);
                    this.host.getNavigator().tryMoveToEntityLiving(this.target, 1);
                } else if(this.seeTime >= 5){
                    this.host.getNavigator().clearPath();
                    this.host.setSprinting(false);
                    this.host.StartAttackingEntity();
                }else {
                    this.host.getNavigator().tryMoveToEntityLiving(this.target, 1);
                }
            }

        }
    }
}

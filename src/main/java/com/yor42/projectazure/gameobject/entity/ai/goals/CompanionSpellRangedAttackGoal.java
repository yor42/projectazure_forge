package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.IMeleeAttacker;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.ISpellUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

public class CompanionSpellRangedAttackGoal extends Goal {
    @Nonnull
    private final ISpellUser host;
    @Nonnull
    private final AbstractEntityCompanion hostEntity;
    @Nullable private LivingEntity target;
    private final float maxAttackDistance;
    private int seeTime;

    public CompanionSpellRangedAttackGoal(@Nonnull AbstractEntityCompanion companion, float maxDistance){
        if(!(companion instanceof ISpellUser)){
            throw new IllegalArgumentException("AbstractEntityCompanion does not implement ISpellUser");
        }
        this.host = (ISpellUser) companion;
        this.hostEntity = companion;
        this.maxAttackDistance = maxDistance;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        @Nullable
        LivingEntity targetEntity = this.hostEntity.getAttackTarget();
        if(targetEntity == null || !targetEntity.isAlive()){
            return false;
        }
        if(this.hostEntity.getOwner() != null && (this.hostEntity.getDistance(this.hostEntity.getOwner())>=16 || targetEntity.getDistance(this.hostEntity.getOwner())>=16)){
            return false;
        }

        if(this.host.shouldUseSpell()) {
            this.target = targetEntity;
            return true;
        }
        return false;
    }

    public boolean shouldContinueExecuting() {
        if(this.target == null || this.hostEntity.getOwner() != null && (this.hostEntity.getDistance(this.hostEntity.getOwner())>=16 || this.target.getDistance(this.hostEntity.getOwner())>=16)){
            return false;
        }

        return this.shouldExecute() || !this.hostEntity.getNavigator().noPath();
    }
    @Override
    public void startExecuting() {
        this.hostEntity.getNavigator().clearPath();
        this.hostEntity.setAggroed(true);
        if(this.target != null) {
            this.hostEntity.faceEntity(this.target, 30.0F, 30.0F);
        }
    }

    @Override
    public void resetTask() {
        this.target = null;
        this.hostEntity.getNavigator().clearPath();
        this.hostEntity.setAggroed(false);
    }

    @Override
    public void tick() {
        if(this.target != null) {
            double distance = this.hostEntity.getDistance(this.target);
            boolean canSee = this.hostEntity.getEntitySenses().canSee(this.target);

            boolean isTooclose = distance <=this.maxAttackDistance*0.5 && !(this.hostEntity instanceof IMeleeAttacker) ;
            boolean isTooFar = distance > (double)this.maxAttackDistance;

            if (canSee) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }
            this.hostEntity.faceEntity(this.target, 30.0F, 30.0F);
            if(!this.hostEntity.isUsingSpell()) {
                if (isTooFar) {
                    this.hostEntity.setSprinting(distance >= this.maxAttackDistance*1.5);
                    this.hostEntity.getNavigator().tryMoveToEntityLiving(this.target, 1);
                } else if (isTooclose) {
                    this.hostEntity.getMoveHelper().strafe(-0.5F, 0);
                    this.hostEntity.setSprinting(false);
                } else if(this.seeTime >= 5){
                    this.hostEntity.getNavigator().clearPath();
                    this.hostEntity.setSprinting(false);
                    this.host.StartShootingEntityUsingSpell(this.target);
                }else {
                    this.hostEntity.setSprinting(distance >= this.maxAttackDistance*1.5);
                    this.hostEntity.getNavigator().tryMoveToEntityLiving(this.target, 1);
                }
            }

        }
    }
}

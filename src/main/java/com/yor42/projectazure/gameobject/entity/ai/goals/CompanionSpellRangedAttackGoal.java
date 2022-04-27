package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.IMeleeAttacker;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.ISpellUser;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

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
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {

        if(this.hostEntity.isCriticallyInjured()){
            return false;
        }

        @Nullable
        LivingEntity targetEntity = this.hostEntity.getTarget();
        if(targetEntity == null || !targetEntity.isAlive()){
            return false;
        }
        if(this.hostEntity.getOwner() != null && (this.hostEntity.distanceTo(this.hostEntity.getOwner())>=16 || targetEntity.distanceTo(this.hostEntity.getOwner())>=16)){
            return false;
        }

        if(this.host.shouldUseSpell()) {
            this.target = targetEntity;
            return true;
        }
        return false;
    }

    public boolean canContinueToUse() {
        if(this.target == null || this.hostEntity.getOwner() != null && (this.hostEntity.distanceTo(this.hostEntity.getOwner())>=16 || this.target.distanceTo(this.hostEntity.getOwner())>=16)){
            return false;
        }

        return this.canUse() || !this.hostEntity.getNavigation().isDone();
    }
    @Override
    public void start() {
        this.hostEntity.getNavigation().stop();
        this.hostEntity.setAggressive(true);
        if(this.target != null) {
            this.hostEntity.lookAt(this.target, 30.0F, 30.0F);
        }
    }

    @Override
    public void stop() {
        this.target = null;
        this.hostEntity.getNavigation().stop();
        this.hostEntity.setAggressive(false);
    }

    @Override
    public void tick() {
        if(this.target != null) {
            double distance = this.hostEntity.distanceTo(this.target);
            boolean canSee = this.hostEntity.getSensing().hasLineOfSight(this.target);

            boolean isTooclose = distance <=this.maxAttackDistance*0.5 && !(this.hostEntity instanceof IMeleeAttacker) ;
            boolean isTooFar = distance > (double)this.maxAttackDistance;

            if (canSee) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }
            this.hostEntity.lookAt(this.target, 30.0F, 30.0F);
            if(!this.hostEntity.isUsingSpell()) {
                if (isTooFar) {
                    this.hostEntity.setSprinting(distance >= this.maxAttackDistance*1.5);
                    this.hostEntity.getNavigation().moveTo(this.target, 1);
                } else if (isTooclose) {
                    this.hostEntity.getMoveControl().strafe(-0.5F, 0);
                    this.hostEntity.setSprinting(false);
                } else if(this.seeTime >= 5){
                    this.hostEntity.getNavigation().stop();
                    this.hostEntity.setSprinting(false);
                    this.host.StartShootingEntityUsingSpell(this.target);
                }else {
                    this.hostEntity.setSprinting(distance >= this.maxAttackDistance*1.5);
                    this.hostEntity.getNavigation().moveTo(this.target, 1);
                }
            }

        }
    }
}

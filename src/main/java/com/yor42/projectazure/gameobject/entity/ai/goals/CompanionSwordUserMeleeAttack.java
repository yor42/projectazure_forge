package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.IMeleeAttacker;
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
    private final AbstractEntityCompanion hostEntity;
    private final IMeleeAttacker host;
    @Nullable private LivingEntity target;
    private int seeTime;

    public CompanionSwordUserMeleeAttack(@Nonnull IMeleeAttacker companion){
        this.host = companion;
        if(!(companion instanceof AbstractEntityCompanion)){
            throw new IllegalArgumentException("IMeleeAttacker companion is not instance of AbstractEntityCompanion!");
        }
        this.hostEntity = (AbstractEntityCompanion) companion;
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

        boolean shouldAttack = this.host.shouldUseNonVanillaAttack(this.hostEntity.getTarget());
        if(shouldAttack) {
            this.target = targetEntity;
            return true;
        }
        return false;
    }

    public boolean canContinueToUse() {

        if(this.target == null){
            return false;
        }

        if(this.hostEntity.getOwner() != null && (this.hostEntity.distanceTo(this.hostEntity.getOwner())>=16 || this.target.distanceTo(this.hostEntity.getOwner())>=16)){
            return false;
        }

        return this.canUse() || !this.hostEntity.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.hostEntity.getNavigation().stop();
        this.hostEntity.setAggressive(true);
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
            boolean canSee = this.hostEntity.getSensing().canSee(this.target);

            boolean isTooFar = distance >= this.host.getAttackRange(this.host.isTalentedWeaponinMainHand());

            if (canSee) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }
            this.hostEntity.lookAt(this.target, 30.0F, 30.0F);
            if(!this.hostEntity.isNonVanillaMeleeAttacking()) {
                if (isTooFar) {
                    this.hostEntity.setSprinting(distance >= 8);
                    this.hostEntity.getNavigation().moveTo(this.target, 1);
                } else if(this.seeTime >= 5){
                    this.hostEntity.getNavigation().stop();
                    this.hostEntity.setSprinting(distance >= 2);
                    this.host.StartMeleeAttackingEntity();
                }else {
                    this.hostEntity.getNavigation().moveTo(this.target, 1);
                }
            }

        }
    }
}

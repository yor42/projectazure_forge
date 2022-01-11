package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.IMeleeAttacker;
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

        boolean shouldAttack = this.host.shouldUseNonVanillaAttack(this.hostEntity.getAttackTarget());
        if(shouldAttack) {
            this.target = targetEntity;
            return true;
        }
        return false;
    }

    public boolean shouldContinueExecuting() {

        if(this.target == null){
            return false;
        }

        if(this.hostEntity.getOwner() != null && (this.hostEntity.getDistance(this.hostEntity.getOwner())>=16 || this.target.getDistance(this.hostEntity.getOwner())>=16)){
            return false;
        }

        return this.shouldExecute() || !this.hostEntity.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.hostEntity.getNavigator().clearPath();
        this.hostEntity.setAggroed(true);
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

            boolean isTooFar = distance >= this.host.getAttackRange(this.host.isUsingTalentedWeapon());

            if (canSee) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }
            this.hostEntity.faceEntity(this.target, 30.0F, 30.0F);
            if(!this.hostEntity.isNonVanillaMeleeAttacking()) {
                if (isTooFar) {
                    this.hostEntity.setSprinting(distance >= 8);
                    this.hostEntity.getNavigator().tryMoveToEntityLiving(this.target, 1);
                } else if(this.seeTime >= 5){
                    this.hostEntity.getNavigator().clearPath();
                    this.hostEntity.setSprinting(distance >= 2);
                    this.host.StartMeleeAttackingEntity();
                }else {
                    this.hostEntity.getNavigator().tryMoveToEntityLiving(this.target, 1);
                }
            }

        }
    }
}

package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.EnumSet;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.getRemainingAmmo;

public class CompanionUseGunGoal extends Goal {

    private final AbstractEntityCompanion companion;
    private final double moveSpeedAmp;
    private final float maxAttackDistance;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public CompanionUseGunGoal(AbstractEntityCompanion companion, float maxAttackDistance, double speed){
        this.companion = companion;
        this.moveSpeedAmp = speed;
        this.maxAttackDistance = maxAttackDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }


    private ItemStack getValidGunStack(){
        if(this.companion.getItemInHand(this.getValidGunHand()).getItem() instanceof ItemGunBase){
            return this.companion.getItemInHand(this.getValidGunHand());
        }
        return ItemStack.EMPTY;
    }

    private Hand getValidGunHand(){
        if(this.companion.getMainHandItem().getItem() instanceof ItemGunBase){
            return Hand.MAIN_HAND;
        }
        else
            return Hand.OFF_HAND;
    }


    @Override
    public boolean canUse() {
        ItemStack gunStack = this.getValidGunStack();

        if(gunStack.getItem() instanceof ItemGunBase) {
            boolean canusegun = this.companion.shouldUseGun();
            boolean hastarget = this.companion.getTarget() != null && this.companion.getTarget().isAlive();
            boolean entitycanAttack = !this.companion.isSleeping() && !this.companion.isOrderedToSit();
            return canusegun && entitycanAttack && hastarget;

        }
        return false;
    }



    public void start() {
        super.start();
        this.companion.setAggressive(true);
        this.companion.getNavigation().stop();
    }

    public void stop() {
        super.stop();
        this.companion.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.companion.stopUsingItem();
        this.companion.setUsingGun(false);
        this.companion.getNavigation().stop();
    }

    @Override
    public boolean canContinueToUse() {
        return (this.canUse() || !this.companion.getNavigation().isDone()) && this.getValidGunStack() != ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        LivingEntity target = this.companion.getTarget();
        if(target != null){
            double distanceSq = this.companion.distanceToSqr(target.getX(), target.getY(), target.getZ());
            boolean canSee = this.companion.getSensing().canSee(target);
            boolean flag1 = this.seeTime > 0;
            if (canSee != flag1) {
                this.seeTime = 0;
            }

            if (canSee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if(!this.companion.isUsingGun()&&this.seeTime>=10 && this.companion.getGunStack() != ItemStack.EMPTY && getRemainingAmmo(this.getValidGunStack())>0){
                this.companion.setUsingGun(true);
            }

            if (!(distanceSq > (double)this.maxAttackDistance) && this.seeTime >= 20) {
                this.companion.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.companion.getNavigation().moveTo(target, this.moveSpeedAmp);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)this.companion.getRandom().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.companion.getRandom().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (distanceSq > (double)(this.maxAttackDistance * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (distanceSq < (double)(this.maxAttackDistance * 0.25F)) {
                    this.strafingBackwards = true;
                }

                this.companion.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.companion.lookAt(target, 30.0F, 30.0F);
            }
            else {
                this.companion.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
            boolean flag = this.companion.isUsingGun();

            if (flag) {
                if (!canSee && this.seeTime < -60) {
                    this.companion.stopUsingItem();
                    this.companion.setUsingGun(false);
                } else if (canSee) {
                    this.companion.setUsingGun(true);
                    boolean hasAmmo = getRemainingAmmo(this.getValidGunStack())>0;
                    boolean reloadable = this.companion.HasRightMagazine(((ItemGunBase) this.getValidGunStack().getItem()).getAmmoType());
                    if(hasAmmo && --this.attackTime <= 0) {
                        this.companion.AttackUsingGun(target, this.getValidGunStack(), this.getValidGunHand());
                        this.attackTime = ((ItemGunBase) this.getValidGunStack().getItem()).getMinFireDelay();
                    }
                    else if(this.getValidGunStack().getItem() instanceof ItemGunBase && getRemainingAmmo(this.getValidGunStack())<=0 && !this.companion.isReloadingMainHand() && reloadable){
                        this.companion.setReloadDelay();
                    }
                }
            } else if (this.seeTime >= -60) {
                this.companion.setUsingGun(true);
                this.companion.startUsingItem(this.getValidGunHand());
            }

        }


    }
}

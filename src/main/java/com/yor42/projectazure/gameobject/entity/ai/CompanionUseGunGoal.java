package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityGunUserBase;
import com.yor42.projectazure.gameobject.items.ItemMagazine;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;

public class CompanionUseGunGoal extends Goal {

    private AbstractEntityCompanion companion;
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
    }


    private ItemStack getValidGunStack(){
        if(this.companion.getHeldItem(this.getValidGunHand()).getItem() instanceof ItemGunBase){
            return this.companion.getHeldItem(this.getValidGunHand());
        }
        return ItemStack.EMPTY;
    }

    private Hand getValidGunHand(){
        if(this.companion.getHeldItemMainhand().getItem() instanceof ItemGunBase){
            return Hand.MAIN_HAND;
        }
        else
            return Hand.OFF_HAND;
    }


    @Override
    public boolean shouldExecute() {

        boolean flag = this.companion.canUseGun() && getValidGunStack().getItem() instanceof ItemGunBase && !this.companion.isSleeping() &&!this.companion.isEntitySleeping()&&
                this.companion.getAttackTarget() != null && this.companion.getAttackTarget().isAlive();

        return flag;
    }



    public void startExecuting() {
        super.startExecuting();
        this.companion.setAggroed(true);
    }

    public void resetTask() {
        super.resetTask();
        this.companion.setAggroed(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.companion.resetActiveHand();
        this.companion.setUsingGun(false);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (this.shouldExecute() || !this.companion.getNavigator().noPath()) && this.getValidGunStack() != ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        LivingEntity target = this.companion.getAttackTarget();
        if(target != null){
            double distanceSq = this.companion.getDistanceSq(target.getPosX(), target.getPosY(), target.getPosZ());
            boolean canSee = this.companion.getEntitySenses().canSee(target);
            boolean seeing = this.seeTime > 0;

            if (canSee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(distanceSq > (double)this.maxAttackDistance) && this.seeTime >= 20) {
                this.companion.getNavigator().clearPath();
                ++this.strafingTime;
            } else {
                this.companion.getNavigator().tryMoveToEntityLiving(target, this.moveSpeedAmp);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)this.companion.getRNG().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.companion.getRNG().nextFloat() < 0.3D) {
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

                this.companion.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.companion.faceEntity(target, 30.0F, 30.0F);
                this.companion.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
            } else {
                this.companion.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
            }

            boolean flag = this.companion.isHandActive() && this.getValidGunStack().getItem() instanceof ItemGunBase;

            if (flag) {
                if (!canSee && this.seeTime < -60) {
                    this.companion.resetActiveHand();
                    this.companion.setUsingGun(false);
                } else if (canSee) {

                    boolean hasAmmo = this.getAmmo(getValidGunStack())>0;

                    if(hasAmmo && --this.attackTime <= 0) {
                        this.companion.AttackUsingGun(target, this.getValidGunStack(), this.getValidGunHand());
                        this.attackTime = ((ItemGunBase) this.getValidGunStack().getItem()).getMinFireDelay();
                    }
                    else{
                        this.companion.setReloadDelay();
                    }
                }
            } else if (this.seeTime >= -60) {
                this.companion.setUsingGun(true);
                this.companion.setActiveHand(this.getValidGunHand());
            }

        }


    }

    public short getAmmo(ItemStack stack){
        if(stack.getItem() instanceof ItemGunBase) {
            CompoundNBT compound = stack.getOrCreateTag();
            return compound.getShort("ammo");
        }
        else return 0;
    }
}

package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class KansenAttackUsingBowGoal extends Goal {
    private EntityKansenBase host;
    private int seeTime;
    private int strafingTime = -1;
    private double moveSpeedAmp;
    private float maxAttackDistance;
    private int attackCooldown;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int attackTime = -1;

    public KansenAttackUsingBowGoal(EntityKansenBase entity, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn){
        this.host = entity;
        this.moveSpeedAmp = moveSpeedAmpIn;
        this.maxAttackDistance = maxAttackDistanceIn;
        this.attackCooldown = attackCooldownIn;
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity target = this.host.getAttackTarget();
        //TODO: check if plane can be used first
        if(target != null && target.isAlive()) {
            for (int i = 0; i < this.host.getShipStorage().getSlots(); i++) {
                if (this.host.getShipStorage().getStackInSlot(i).getItem() instanceof ArrowItem) {
                    return this.host.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() instanceof BowItem;
                }
            }
        }
        return false;
    }

    public boolean shouldContinueExecuting() {
        return (this.shouldExecute() || !this.host.getNavigator().noPath()) && this.host.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() instanceof BowItem;
    }

    public void startExecuting() {
        super.startExecuting();
        this.host.setAggroed(true);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.host.setAggroed(false);
        this.seeTime = 0;
        this.host.setUsingbow(false);
        this.host.resetActiveHand();
        this.attackTime = -1;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity targetEntity = this.host.getAttackTarget();
        if(targetEntity != null){
            double distance = this.host.getDistanceSq(targetEntity);
            boolean cansee = this.host.getEntitySenses().canSee(targetEntity);
            boolean isSeeing = this.seeTime>0;

            if (cansee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(distance > (double)this.maxAttackDistance) && this.seeTime >= 20) {
                this.host.getNavigator().clearPath();
                ++this.strafingTime;
            } else {
                this.host.getNavigator().tryMoveToEntityLiving(targetEntity, this.moveSpeedAmp);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)this.host.getRNG().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.host.getRNG().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (distance > (double)(this.maxAttackDistance * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (distance < (double)(this.maxAttackDistance * 0.25F)) {
                    this.strafingBackwards = true;
                }

                this.host.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.host.faceEntity(targetEntity, 30.0F, 30.0F);
            } else {
                this.host.getLookController().setLookPositionWithEntity(targetEntity, 30.0F, 30.0F);
            }

            if (this.host.isHandActive()) {
                if (!cansee && this.seeTime < -60) {
                    this.host.resetActiveHand();
                } else if (cansee) {
                    int i = this.host.getItemInUseMaxCount();
                    if (i >= 20) {
                        this.host.resetActiveHand();
                        this.host.ShootArrow(targetEntity, BowItem.getArrowVelocity(i));
                        this.attackTime = this.attackCooldown;
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                this.host.setActiveHand(Hand.MAIN_HAND);
                this.host.setUsingbow(true);
            }

        }
    }
}

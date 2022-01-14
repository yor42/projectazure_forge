package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class KansenAttackUsingBowGoal extends Goal {
    private EntityKansenBase host;
    private int seeTime;
    private int strafingTime = -1;
    private final double moveSpeedAmp;
    private final float maxAttackDistance;
    private int attackCooldown;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int attackTime = -1;

    public KansenAttackUsingBowGoal(EntityKansenBase entity, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn){
        this.host = entity;
        this.moveSpeedAmp = moveSpeedAmpIn;
        this.maxAttackDistance = maxAttackDistanceIn;
        this.attackCooldown = attackCooldownIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.host.getTarget();
        if(target != null && target.isAlive() && this.host.getItemBySlot(EquipmentSlotType.MAINHAND).getItem() instanceof BowItem) {
            for (int i = 0; i < this.host.getInventory().getSlots(); i++) {
                if (this.host.getInventory().getStackInSlot(i).getItem() instanceof ArrowItem) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.host.getNavigation().isDone()) && this.host.getItemBySlot(EquipmentSlotType.MAINHAND).getItem() instanceof BowItem;
    }

    public void start() {
        super.start();
        this.host.setAggressive(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.host.setAggressive(false);
        this.seeTime = 0;
        this.host.setUsingbow(false);
        this.host.stopUsingItem();
        this.attackTime = -1;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity targetEntity = this.host.getTarget();
        if(targetEntity != null){
            double distance = this.host.distanceToSqr(targetEntity);
            boolean cansee = this.host.getSensing().canSee(targetEntity);
            boolean isSeeing = this.seeTime>0;

            if (cansee != isSeeing) {
                this.seeTime = 0;
            }

            if (cansee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(distance > (double)this.maxAttackDistance) && this.seeTime >= 20) {
                this.host.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.host.getNavigation().moveTo(targetEntity, this.moveSpeedAmp);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)this.host.getRandom().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.host.getRandom().nextFloat() < 0.3D) {
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

                this.host.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.host.lookAt(targetEntity, 30.0F, 30.0F);
            } else {
                this.host.getLookControl().setLookAt(targetEntity, 30.0F, 30.0F);
            }

            if (this.host.isUsingItem()) {
                if (!cansee && this.seeTime < -60) {
                    this.host.stopUsingItem();
                } else if (cansee) {
                    int i = this.host.getTicksUsingItem();
                    if (i >= 20) {
                        this.host.stopUsingItem();
                        this.host.ShootArrow(targetEntity, BowItem.getPowerForTime(i));
                        this.attackTime = this.attackCooldown;
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                this.host.startUsingItem(Hand.MAIN_HAND);
                this.host.setUsingbow(true);
            }

        }
    }
}

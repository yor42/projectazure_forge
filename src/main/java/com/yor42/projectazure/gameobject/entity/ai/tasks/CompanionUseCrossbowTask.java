package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

public class CompanionUseCrossbowTask extends Behavior<AbstractEntityCompanion> {

    private int attackDelay;
    private Status crossbowState = Status.UNCHARGED;
    
    public CompanionUseCrossbowTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
    }

    protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, AbstractEntityCompanion p_212832_2_) {

        if(!p_212832_2_.isHolding(item -> item.getItem() instanceof CrossbowItem) ){
            return false;
        }
        ItemStack crossbow = p_212832_2_.getItemInHand(ProjectileUtil.getWeaponHoldingHand(p_212832_2_, item -> item instanceof CrossbowItem));
        ItemStack ammo = p_212832_2_.getProjectile(crossbow);
        if(ammo.isEmpty()){
            return false;
        }

        LivingEntity livingentity = getAttackTarget(p_212832_2_);
        return BehaviorUtils.canSee(p_212832_2_, livingentity) && BehaviorUtils.isWithinAttackRange(p_212832_2_, livingentity, 0);
    }

    protected boolean canStillUse(ServerLevel p_212834_1_, AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        return p_212834_2_.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(p_212834_1_, p_212834_2_);
    }

    protected void tick(ServerLevel p_212833_1_, AbstractEntityCompanion p_212833_2_, long p_212833_3_) {
        LivingEntity livingentity = getAttackTarget(p_212833_2_);
        this.lookAtTarget(p_212833_2_, livingentity);
        this.crossbowAttack(p_212833_2_, livingentity);
    }

    protected void stop(ServerLevel p_212835_1_, AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        if (p_212835_2_.isUsingItem()) {
            p_212835_2_.stopUsingItem();
        }

        if (p_212835_2_.isHolding(item -> item.getItem() instanceof CrossbowItem)) {
            p_212835_2_.setChargingCrossbow(false);
            CrossbowItem.setCharged(p_212835_2_.getUseItem(), false);
        }

    }

    private void crossbowAttack(AbstractEntityCompanion p_233888_1_, LivingEntity p_233888_2_) {

        if(!p_233888_2_.isHolding(item -> item.getItem() instanceof CrossbowItem) ){
            return;
        }
        ItemStack crossbow = p_233888_2_.getItemInHand(ProjectileUtil.getWeaponHoldingHand(p_233888_2_, item -> item instanceof CrossbowItem));
        ItemStack ammo = p_233888_2_.getProjectile(crossbow);
        if (this.crossbowState == Status.UNCHARGED) {
            if(!ammo.isEmpty()) {
                p_233888_1_.startUsingItem(ProjectileUtil.getWeaponHoldingHand(p_233888_1_, item -> item instanceof CrossbowItem));
                this.crossbowState = Status.CHARGING;
                p_233888_1_.setChargingCrossbow(true);
            }
        } else if (this.crossbowState == Status.CHARGING) {
            if (!p_233888_1_.isUsingItem() || ammo.isEmpty()) {
                this.crossbowState = Status.UNCHARGED;
            }

            int i = p_233888_1_.getTicksUsingItem();
            ItemStack itemstack = p_233888_1_.getUseItem();
            if (i >= CrossbowItem.getChargeDuration(itemstack)) {
                p_233888_1_.releaseUsingItem();
                this.crossbowState = Status.CHARGED;
                this.attackDelay = 20 + p_233888_1_.getRandom().nextInt(20);
                p_233888_1_.setChargingCrossbow(false);
            }
        } else if (this.crossbowState == Status.CHARGED) {
            --this.attackDelay;
            if (this.attackDelay == 0) {
                this.crossbowState = Status.READY_TO_ATTACK;
            }
        } else if (this.crossbowState == Status.READY_TO_ATTACK) {
            p_233888_1_.performRangedAttack(p_233888_2_, 1.0F);
            ItemStack itemstack1 = p_233888_1_.getItemInHand(ProjectileUtil.getWeaponHoldingHand(p_233888_1_, item -> item instanceof CrossbowItem));
            CrossbowItem.setCharged(itemstack1, false);
            this.crossbowState = Status.UNCHARGED;
        }

    }

    private void lookAtTarget(Mob p_233889_1_, LivingEntity p_233889_2_) {
        p_233889_1_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(p_233889_2_, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity p_233887_0_) {
        return p_233887_0_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

    enum Status {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK
    }
}

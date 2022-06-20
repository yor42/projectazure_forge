package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.world.server.ServerWorld;

public class CompanionUseCrossbowTask extends Task<AbstractEntityCompanion> {

    private int attackDelay;
    private Status crossbowState = Status.UNCHARGED;
    
    public CompanionUseCrossbowTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT), 1200);
    }

    protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, AbstractEntityCompanion p_212832_2_) {

        if(!p_212832_2_.isHolding(item -> item instanceof CrossbowItem) ){
            return false;
        }
        ItemStack crossbow = p_212832_2_.getItemInHand(ProjectileHelper.getWeaponHoldingHand(p_212832_2_, item -> item instanceof CrossbowItem));
        ItemStack ammo = p_212832_2_.getProjectile(crossbow);
        if(ammo.isEmpty()){
            return false;
        }

        LivingEntity livingentity = getAttackTarget(p_212832_2_);
        return BrainUtil.canSee(p_212832_2_, livingentity) && BrainUtil.isWithinAttackRange(p_212832_2_, livingentity, 0);
    }

    protected boolean canStillUse(ServerWorld p_212834_1_, AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        return p_212834_2_.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(p_212834_1_, p_212834_2_);
    }

    protected void tick(ServerWorld p_212833_1_, AbstractEntityCompanion p_212833_2_, long p_212833_3_) {
        LivingEntity livingentity = getAttackTarget(p_212833_2_);
        this.lookAtTarget(p_212833_2_, livingentity);
        this.crossbowAttack(p_212833_2_, livingentity);
    }

    protected void stop(ServerWorld p_212835_1_, AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        if (p_212835_2_.isUsingItem()) {
            p_212835_2_.stopUsingItem();
        }

        if (p_212835_2_.isHolding(item -> item instanceof CrossbowItem)) {
            p_212835_2_.setChargingCrossbow(false);
            CrossbowItem.setCharged(p_212835_2_.getUseItem(), false);
        }

    }

    private void crossbowAttack(AbstractEntityCompanion p_233888_1_, LivingEntity p_233888_2_) {

        if(!p_233888_2_.isHolding(item -> item instanceof CrossbowItem) ){
            return;
        }
        ItemStack crossbow = p_233888_2_.getItemInHand(ProjectileHelper.getWeaponHoldingHand(p_233888_2_, item -> item instanceof CrossbowItem));
        ItemStack ammo = p_233888_2_.getProjectile(crossbow);
        if (this.crossbowState == Status.UNCHARGED) {
            if(!ammo.isEmpty()) {
                p_233888_1_.startUsingItem(ProjectileHelper.getWeaponHoldingHand(p_233888_1_, item -> item instanceof CrossbowItem));
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
            ItemStack itemstack1 = p_233888_1_.getItemInHand(ProjectileHelper.getWeaponHoldingHand(p_233888_1_, item -> item instanceof CrossbowItem));
            CrossbowItem.setCharged(itemstack1, false);
            this.crossbowState = Status.UNCHARGED;
        }

    }

    private void lookAtTarget(MobEntity p_233889_1_, LivingEntity p_233889_2_) {
        p_233889_1_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityPosWrapper(p_233889_2_, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity p_233887_0_) {
        return p_233887_0_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

    enum Status {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK;
    }
}

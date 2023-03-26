package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.server.level.ServerLevel;

import static net.minecraft.world.entity.ai.memory.MemoryModuleType.ATTACK_TARGET;

public class CompanionBowRangedAttackTask extends Behavior<AbstractEntityCompanion> {
    private int seeTime;
    private int attackTime = -1;


    public CompanionBowRangedAttackTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel p_212832_1_, AbstractEntityCompanion entity) {
        return !entity.getProjectile(entity.getMainHandItem()).isEmpty();
    }

    @Override
    protected boolean canStillUse(ServerLevel p_212834_1_, AbstractEntityCompanion entity, long p_212834_3_) {
        return this.checkExtraStartConditions(p_212834_1_, entity) && entity.getBrain().getMemory(ATTACK_TARGET).map((target)->!target.isDeadOrDying()).orElse(true);
    }

    @Override
    protected void start(ServerLevel p_212831_1_, AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        super.start(p_212831_1_, p_212831_2_, p_212831_3_);
        p_212831_2_.setAggressive(true);
    }

    @Override
    protected void stop(ServerLevel p_212835_1_, AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        this.seeTime = 0;
        this.attackTime = -1;
        p_212835_2_.setAggressive(false);
        p_212835_2_.stopUsingItem();
    }

    @Override
    protected void tick(ServerLevel p_212833_1_, AbstractEntityCompanion mob, long p_212833_3_) {
        Brain<AbstractEntityCompanion> brain = mob.getBrain();
        if(!brain.getMemory(ATTACK_TARGET).isPresent()){
            return;
        }

        LivingEntity target = brain.getMemory(ATTACK_TARGET).get();

        double d0 = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
        boolean flag = mob.getSensing().hasLineOfSight(target);
        boolean flag1 = this.seeTime > 0;
        if (flag != flag1) {
            this.seeTime = 0;
        }

        if (flag) {
            ++this.seeTime;
        } else {
            --this.seeTime;
        }

        if (!(d0 > 15*15) && this.seeTime >= 5) {
            this.clearWalkTarget(mob);
        } else {
            BehaviorUtils.setWalkAndLookTargetMemories(mob, target, 1.0F, (int)mob.getBowRange()-2);
        }

        mob.lookAt(target, 30.0F, 30.0F);
        if (mob.isUsingItem()) {
            if (!flag && this.seeTime < -60) {
                mob.stopUsingItem();
            } else if (flag) {
                int i = mob.getTicksUsingItem();
                if (i >= 20) {
                    mob.stopUsingItem();
                    mob.performRangedAttack(target, BowItem.getPowerForTime(i));
                    this.attackTime = 20;
                }
            }
        } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
            mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(mob, item -> item instanceof BowItem));
        }
    }

    private void clearWalkTarget(LivingEntity p_233967_1_) {
        p_233967_1_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }
}

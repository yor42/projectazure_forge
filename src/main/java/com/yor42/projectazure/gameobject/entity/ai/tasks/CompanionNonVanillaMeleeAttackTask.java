package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IMeleeAttacker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;

import static net.minecraft.world.entity.ai.memory.MemoryModuleType.*;

public class CompanionNonVanillaMeleeAttackTask extends Behavior<AbstractEntityCompanion> {
    public CompanionNonVanillaMeleeAttackTask() {
        super(ImmutableMap.of(LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel p_212832_1_, @Nonnull AbstractEntityCompanion p_212832_2_) {
        LivingEntity target = getAttackTarget(p_212832_2_);

        if(p_212832_2_.isSwimming() && p_212832_2_.isOrderedToSit() && p_212832_2_.isPassenger()){
            return false;
        }
        if (!(p_212832_2_ instanceof IMeleeAttacker)) {
            return false;
        }

        if(!BehaviorUtils.isWithinAttackRange(p_212832_2_, target, 0)){
            return false;
        }

        if(!p_212832_2_.wantsToAttack(target, p_212832_2_)){
            return false;
        }
        boolean flag = ((IMeleeAttacker) p_212832_2_).shouldUseNonVanillaAttack(target) && BehaviorUtils.canSee(p_212832_2_, target);
        return flag && !p_212832_2_.isNonVanillaMeleeAttacking();
    }

    @Override
    protected void start(@Nonnull ServerLevel p_212831_1_, @Nonnull AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        if (p_212831_2_ instanceof IMeleeAttacker) {
            ((IMeleeAttacker) p_212831_2_).StartMeleeAttackingEntity();
            p_212831_2_.setMeleeAttackDelay((int) (((IMeleeAttacker) p_212831_2_).MeleeAttackAnimationLength() * ((IMeleeAttacker) p_212831_2_).getAttackSpeedModifier(((IMeleeAttacker) p_212831_2_).isTalentedWeaponinMainHand())));
            p_212831_2_.StartedMeleeAttackTimeStamp = p_212831_2_.tickCount;
        }
    }

    @Override
    protected boolean canStillUse(ServerLevel p_212834_1_, AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        if(!p_212834_2_.getBrain().hasMemoryValue(ATTACK_TARGET) || getAttackTarget(p_212834_2_).isDeadOrDying()){
            return false;
        }

        int currentspelldelay = p_212834_2_.getNonVanillaMeleeAttackDelay();
        return currentspelldelay > 0;
    }

    @Override
    protected void tick(ServerLevel p_212833_1_, AbstractEntityCompanion p_212833_2_, long p_212833_3_) {
        int currentspelldelay = p_212833_2_.getNonVanillaMeleeAttackDelay();
        if (currentspelldelay > 0) {
            p_212833_2_.getBrain().eraseMemory(WALK_TARGET);
            LivingEntity target = getAttackTarget(p_212833_2_);
            if (p_212833_2_.isSprinting()) {
                p_212833_2_.setSprinting(false);
            }
            p_212833_2_.lookAt(target, 30.0F, 30.0F);
            int delay = p_212833_2_.tickCount - p_212833_2_.StartedMeleeAttackTimeStamp;
            if (!((IMeleeAttacker) p_212833_2_).MeleeAttackAudioCue().isEmpty() && ((IMeleeAttacker) p_212833_2_).MeleeAttackAudioCue().contains(delay)) {
                p_212833_2_.playMeleeAttackPreSound();
            }
            if (((IMeleeAttacker) p_212833_2_).getAttackDamageDelay().contains(delay) && p_212833_2_.distanceTo(target) <= ((IMeleeAttacker) p_212833_2_).getAttackRange(((IMeleeAttacker) p_212833_2_).isTalentedWeaponinMainHand())) {
                p_212833_2_.AttackCount += 1;
                ((IMeleeAttacker) p_212833_2_).PerformMeleeAttack(target, (float) p_212833_2_.getAttribute(Attributes.ATTACK_DAMAGE).getValue(), p_212833_2_.AttackCount);
            }

        }
    }

    private static LivingEntity getAttackTarget(LivingEntity p_233887_0_) {
        return p_233887_0_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}

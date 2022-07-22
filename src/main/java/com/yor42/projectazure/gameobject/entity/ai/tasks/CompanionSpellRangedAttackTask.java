package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.ISpellUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.*;

public class CompanionSpellRangedAttackTask extends Task<AbstractEntityCompanion> {
    public CompanionSpellRangedAttackTask() {
        super(ImmutableMap.of(LOOK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT), 1200);
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerWorld p_212832_1_, @Nonnull AbstractEntityCompanion p_212832_2_) {
        LivingEntity target = getAttackTarget(p_212832_2_);

        if(!(p_212832_2_ instanceof  ISpellUser)){
            return false;
        }

        if(!p_212832_2_.closerThan(target, p_212832_2_.getSpellRange())){
            return false;
        }

        if(!p_212832_2_.wantsToAttack(target, p_212832_2_)){
            return false;
        }
        boolean flag = ((ISpellUser) p_212832_2_).shouldUseSpell(target) && p_212832_2_.RangedAttackCoolDown<=0 && BrainUtil.canSee(p_212832_2_, target);
        return flag && !p_212832_2_.isUsingSpell();
    }

    @Override
    protected void start(@Nonnull ServerWorld p_212831_1_, @Nonnull AbstractEntityCompanion entity, long p_212831_3_) {
        LivingEntity target = getAttackTarget(entity);
        if (entity instanceof ISpellUser) {
            if(!entity.closerThan(target, entity.getSpellRange())){
                BrainUtil.setWalkAndLookTargetMemories(entity, target, 1, (int) (entity.getSpellRange()-2));
            }
            else {
                this.clearWalkTarget(entity);
                ((ISpellUser) entity).StartSpellAttack(target);
                entity.setSpellDelay(((ISpellUser) entity).getInitialSpellDelay());
                entity.StartedSpellAttackTimeStamp = entity.tickCount;
                entity.RangedAttackCoolDown = ((ISpellUser) entity).SpellCooldown();
            }
        }
    }

    @Override
    protected boolean canStillUse(ServerWorld p_212834_1_, AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        if(!p_212834_2_.getBrain().hasMemoryValue(ATTACK_TARGET) || getAttackTarget(p_212834_2_).isDeadOrDying()){
            return false;
        }

        int currentspelldelay = p_212834_2_.getSpellDelay();
        return currentspelldelay > 0;
    }

    @Override
    protected void tick(ServerWorld p_212833_1_, AbstractEntityCompanion p_212833_2_, long p_212833_3_) {
        p_212833_2_.getBrain().eraseMemory(WALK_TARGET);
        if(p_212833_2_.isSprinting()){
            p_212833_2_.setSprinting(false);
        }
        @Nullable
        LivingEntity target = p_212833_2_.getBrain().getMemory(ATTACK_TARGET).orElse(null);
        int delay = p_212833_2_.tickCount - p_212833_2_.StartedSpellAttackTimeStamp;
        if(target!=null) {
            p_212833_2_.lookAt(target, 90F, 90F);
            if (delay == ((ISpellUser) p_212833_2_).getProjectilePreAnimationDelay()) {
                ((ISpellUser) p_212833_2_).ShootProjectile(p_212833_1_, target);
            }
        }
    }

    @Override
    protected void stop(ServerWorld p_212835_1_, AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        p_212835_2_.setSpellDelay(0);
        super.stop(p_212835_1_, p_212835_2_, p_212835_3_);
    }

    private void clearWalkTarget(LivingEntity p_233967_1_) {
        p_233967_1_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    private static LivingEntity getAttackTarget(LivingEntity p_233887_0_) {
        return p_233887_0_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}

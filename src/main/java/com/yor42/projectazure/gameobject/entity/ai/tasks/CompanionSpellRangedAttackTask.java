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

import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.LOOK_TARGET;

public class CompanionSpellRangedAttackTask extends Task<AbstractEntityCompanion> {
    public CompanionSpellRangedAttackTask() {
        super(ImmutableMap.of(LOOK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT), 1200);
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerWorld p_212832_1_, @Nonnull AbstractEntityCompanion p_212832_2_) {
        LivingEntity target = getAttackTarget(p_212832_2_);
        if (p_212832_2_ instanceof ISpellUser) {
            boolean flag = ((ISpellUser) p_212832_2_).shouldUseSpell(target) && p_212832_2_.RangedAttackCoolDown<=0 && BrainUtil.canSee(p_212832_2_, target);
            return flag && !p_212832_2_.isUsingSpell();
        }
        return false;
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
                ((ISpellUser) entity).StartShootingEntityUsingSpell(target);
                entity.RangedAttackCoolDown = ((ISpellUser) entity).SpellCooldown();
            }
        }
    }

    private void clearWalkTarget(LivingEntity p_233967_1_) {
        p_233967_1_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    private static LivingEntity getAttackTarget(LivingEntity p_233887_0_) {
        return p_233887_0_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}

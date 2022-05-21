package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.IMeleeAttacker;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.CrossbowItem;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Map;

import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.LOOK_TARGET;

public class CompanionNonVanillaMeleeAttackTask extends Task<AbstractEntityCompanion> {
    public CompanionNonVanillaMeleeAttackTask() {
        super(ImmutableMap.of(LOOK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT), 1200);
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerWorld p_212832_1_, @Nonnull AbstractEntityCompanion p_212832_2_) {
        LivingEntity target = getAttackTarget(p_212832_2_);
        if (p_212832_2_ instanceof IMeleeAttacker) {
            boolean flag = ((IMeleeAttacker) p_212832_2_).shouldUseNonVanillaAttack(target) && BrainUtil.canSee(p_212832_2_, target) && BrainUtil.isWithinAttackRange(p_212832_2_, target, 0);
            return flag && !p_212832_2_.isNonVanillaMeleeAttacking();
        }
        return false;
    }

    @Override
    protected void start(@Nonnull ServerWorld p_212831_1_, @Nonnull AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        if (p_212831_2_ instanceof IMeleeAttacker) {
            ((IMeleeAttacker) p_212831_2_).StartMeleeAttackingEntity();
        }
    }

    private static LivingEntity getAttackTarget(LivingEntity p_233887_0_) {
        return p_233887_0_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}
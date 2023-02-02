package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.ATTACK_TARGET;

public class CompanionUseSkillTask extends Task<AbstractEntityCompanion> {

    private int SkillTimer=0;

    public CompanionUseSkillTask() {
        super(ImmutableMap.of(ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld p_212832_1_, AbstractEntityCompanion companion) {
        return companion.getBrain().getMemory(ATTACK_TARGET).map(companion::canUseSkill).orElse(false);
    }

    @Override
    protected boolean canStillUse(ServerWorld p_212834_1_, AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        return true;
    }

    @Override
    protected void start(ServerWorld p_212831_1_, AbstractEntityCompanion companion, long p_212831_3_) {
        companion.getBrain().getMemory(ATTACK_TARGET).ifPresent((target)->{
            companion.setUsingSkill(true);
            if(companion.performOneTimeSkill(target)){
                doStop(p_212831_1_, companion, p_212831_3_);
            }
        });
    }

    @Override
    protected void stop(ServerWorld p_212835_1_, AbstractEntityCompanion companion, long p_212835_3_) {
        companion.resetSkill();
        companion.setSkillDelay();
        companion.setUsingSkill(false);
        this.SkillTimer = 0;
    }

    @Override
    protected void tick(ServerWorld p_212833_1_, AbstractEntityCompanion companion, long p_212833_3_) {

        companion.getBrain().getMemory(ATTACK_TARGET).ifPresent((target)->{
            if(companion.performSkillTick(target, this.SkillTimer)){
                this.SkillTimer = 0;
                this.doStop(p_212833_1_, companion, p_212833_3_);
            }
        });
        this.SkillTimer++;
    }
}

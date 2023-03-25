package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;

import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.ATTACK_TARGET;

public clasnet.minecraft.world.entity.ai.memory.MemoryModuleTypetyCompanion> {

    private int SkillTimer=0;

    public CompanionUseSkillTask() {
        super(ImmutableMap.of(ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel p_212832_1_, AbstractEntityCompanion companion) {
        return companion.getBrain().getMemory(ATTACK_TARGET).map(companion::canUseSkill).orElse(false);
    }

    @Override
    protected boolean canStillUse(@Nonnull ServerLevel p_212834_1_, @Nonnull AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        return true;
    }

    @Override
    protected void start(@Nonnull ServerLevel p_212831_1_, AbstractEntityCompanion companion, long p_212831_3_) {
        companion.getBrain().getMemory(ATTACK_TARGET).ifPresent((target)->{
            companion.setUsingSkill(true);
            if(companion.performOneTimeSkill(target)){
                doStop(p_212831_1_, companion, p_212831_3_);
            }
        });
    }

    @Override
    protected void stop(@Nonnull ServerLevel p_212835_1_, AbstractEntityCompanion companion, long p_212835_3_) {
        companion.resetSkill();
        companion.setSkillDelay();
        companion.setUsingSkill(false);
        this.SkillTimer = 0;
    }

    @Override
    protected void tick(@Nonnull ServerLevel p_212833_1_, AbstractEntityCompanion companion, long p_212833_3_) {

        companion.getBrain().getMemory(ATTACK_TARGET).ifPresent((target)->{
            if(companion.performSkillTick(target, this.SkillTimer)){
                this.SkillTimer = 0;
                this.doStop(p_212833_1_, companion, p_212833_3_);
            }
        });
        this.SkillTimer++;
    }
}

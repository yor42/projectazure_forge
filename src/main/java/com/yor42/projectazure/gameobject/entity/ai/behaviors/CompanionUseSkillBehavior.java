package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import javax.annotation.Nonnull;
import java.util.List;

import static net.minecraft.world.entity.ai.memory.MemoryModuleType.ATTACK_TARGET;

public class CompanionUseSkillBehavior extends ExtendedBehaviour<AbstractEntityCompanion> {
    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    private int SkillTimer=0;


    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {
        return entity.canUseSkill(BrainUtils.getTargetOfEntity(entity));
    }

    @Override
    protected boolean canStillUse(@Nonnull ServerLevel p_212834_1_, @Nonnull AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        return true;
    }

    @Override
    protected void start(@Nonnull ServerLevel p_212831_1_, AbstractEntityCompanion companion, long p_212831_3_) {
        LivingEntity target =  BrainUtils.getTargetOfEntity(companion);
        companion.setUsingSkill(true);
        if(companion.performOneTimeSkill(target)){
            doStop(p_212831_1_, companion, p_212831_3_);
        }
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

package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IWorldSkillUseable;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.Optional;

import static com.yor42.projectazure.setup.register.RegisterAI.NEAREST_WORLDSKILLABLE;

public class CompanionUseWorldSkill extends Behavior<AbstractEntityCompanion> {

    public CompanionUseWorldSkill() {
        super(ImmutableMap.of(NEAREST_WORLDSKILLABLE.get(), MemoryStatus.VALUE_PRESENT), Integer.MAX_VALUE);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, AbstractEntityCompanion entity) {

        if(entity.isAngry()){
            return false;
        }

        Brain<AbstractEntityCompanion> brain = entity.getBrain();

        Optional<BlockPos> skillpos = brain.getMemory(NEAREST_WORLDSKILLABLE.get());

        if(!skillpos.isPresent()){
            return false;
        }

        if(entity instanceof IWorldSkillUseable){
            if(!((IWorldSkillUseable) entity).canUseWorldSkill(world, skillpos.get(), entity)){
                return false;
            }

            return skillpos.get().closerToCenterThan(entity.position(), ((IWorldSkillUseable)entity).getWorldSkillRange());
        }

        return false;
    }

    @Override
    protected boolean canStillUse(ServerLevel p_212834_1_, AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        return this.checkExtraStartConditions(p_212834_1_,p_212834_2_);
    }

    @Override
    protected void start(ServerLevel world, AbstractEntityCompanion entity, long p_212831_3_) {
        if(entity instanceof IWorldSkillUseable){
            entity.setUsingWorldSkill(true);
            Brain<AbstractEntityCompanion> brain = entity.getBrain();

            if(brain.getMemory(NEAREST_WORLDSKILLABLE.get()).map((pos)-> ((IWorldSkillUseable) entity).executeWorldSkill(world, pos, entity)).orElse(true)){
                this.doStop(world, entity, p_212831_3_);
            }
        }
    }

    @Override
    protected void stop(ServerLevel p_212835_1_, AbstractEntityCompanion entity, long p_212835_3_) {
        entity.setUsingWorldSkill(false);
        if(entity instanceof IWorldSkillUseable){
            entity.getBrain().getMemory(NEAREST_WORLDSKILLABLE.get()).ifPresent((pos)->{
                ((IWorldSkillUseable) entity).endWorldSkill(p_212835_1_, pos, entity);
            });
        }
        entity.getBrain().eraseMemory(NEAREST_WORLDSKILLABLE.get());
        entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Override
    protected void tick(ServerLevel world, AbstractEntityCompanion entity, long p_212833_3_) {
        if(entity instanceof IWorldSkillUseable){
            Brain<AbstractEntityCompanion> brain = entity.getBrain();

            if(brain.getMemory(NEAREST_WORLDSKILLABLE.get()).map((pos)-> ((IWorldSkillUseable) entity).executeWorldSkillTick(world, pos, entity)).orElse(true)){
                this.doStop(world, entity, p_212833_3_);
            }
        }
    }
}

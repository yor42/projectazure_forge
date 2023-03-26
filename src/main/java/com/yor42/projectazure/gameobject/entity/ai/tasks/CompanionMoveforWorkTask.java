package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IWorldSkillUseable;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.setup.register.RegisterAI.NEAREST_WORLDSKILLABLE;

public class CompanionMoveforWorkTask extends Behavior<AbstractEntityCompanion> {
    private final float MaxDistance;
    private BlockPos position;
    private boolean isWorldSkill = false;
    public CompanionMoveforWorkTask(float maxDistance) {
        super(ImmutableMap.of(RegisterAI.NEAREST_ORE.get(), MemoryStatus.REGISTERED, RegisterAI.NEAREST_HARVESTABLE.get(), MemoryStatus.REGISTERED, RegisterAI.NEAREST_PLANTABLE.get(), MemoryStatus.REGISTERED, RegisterAI.NEAREST_BONEMEALABLE.get(), MemoryStatus.REGISTERED));
        this.MaxDistance = maxDistance;
    }

    public CompanionMoveforWorkTask() {
        this(12);
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel p_212832_1_, AbstractEntityCompanion entity) {

        if(entity.isAngry()){
            return false;
        }

        Brain<AbstractEntityCompanion> brain = entity.getBrain();

        if(entity instanceof IWorldSkillUseable){
            boolean worldskill = brain.getMemory(NEAREST_WORLDSKILLABLE.get()).map((pos)->{
                this.position = pos;
                if(entity.getOwner()!=null){
                    return pos.closerThan(entity.getOwner().blockPosition(), this.MaxDistance);
                }

                return pos.closerThan(entity.blockPosition(), this.MaxDistance);
            }).orElse(false);
            if(worldskill){
                this.isWorldSkill = true;
                return true;
            }
        }

        if(entity.shouldHelpMine()){
            return brain.getMemory(RegisterAI.NEAREST_ORE.get()).map((pos)-> {
                this.position = pos;
                if(entity.getOwner()!=null){
                    return pos.closerThan(entity.getOwner().blockPosition(), this.MaxDistance);
                }

                return pos.closerThan(entity.blockPosition(), this.MaxDistance);}).orElse(false);
        }
        else if(entity.shouldHelpFarm()){

        }
        return false;
    }

    @Override
    protected void start(ServerLevel p_212831_1_, AbstractEntityCompanion entity, long p_212831_3_) {
        if(entity instanceof IWorldSkillUseable && this.isWorldSkill){
            BehaviorUtils.setWalkAndLookTargetMemories(entity, this.position,1, (int) ((IWorldSkillUseable) entity).getWorldSkillRange());
            this.isWorldSkill = false;
        }
        else {
            BehaviorUtils.setWalkAndLookTargetMemories(entity, this.position, 1, 1);
        }
    }
}

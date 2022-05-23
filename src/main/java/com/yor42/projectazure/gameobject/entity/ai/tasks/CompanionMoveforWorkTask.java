package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.HoeItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Map;

public class CompanionMoveforWorkTask extends Task<AbstractEntityCompanion> {
    private final float MaxDistance;
    private BlockPos position;
    public CompanionMoveforWorkTask(float maxDistance) {
        super(ImmutableMap.of(registerManager.NEAREST_ORE.get(), MemoryModuleStatus.REGISTERED, registerManager.NEAREST_HARVESTABLE.get(), MemoryModuleStatus.REGISTERED, registerManager.NEAREST_PLANTABLE.get(), MemoryModuleStatus.REGISTERED, registerManager.NEAREST_BONEMEALABLE.get(), MemoryModuleStatus.REGISTERED));
        this.MaxDistance = maxDistance;
    }

    public CompanionMoveforWorkTask() {
        this(12);
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerWorld p_212832_1_, AbstractEntityCompanion entity) {
        Brain<AbstractEntityCompanion> brain = entity.getBrain();
        if(entity.shouldHelpMine()){
            return brain.getMemory(registerManager.NEAREST_ORE.get()).map((pos)-> {
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
    protected void start(ServerWorld p_212831_1_, AbstractEntityCompanion entity, long p_212831_3_) {
        BrainUtil.setWalkAndLookTargetMemories(entity, this.position,1, 4);
    }
}

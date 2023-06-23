package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class CompanionWakeupBehavior extends ExtendedBehaviour<AbstractEntityCompanion> {

    public CompanionWakeupBehavior(){
        this.startCondition((entity)->{
            if(entity.isCriticallyInjured()){
                return false;
            }
            return entity.level.isDay();
        });
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {
        entity.stopSleeping();
        if(entity.shouldBeSitting){
            entity.setOrderedToSit(true);
        }
    }
}

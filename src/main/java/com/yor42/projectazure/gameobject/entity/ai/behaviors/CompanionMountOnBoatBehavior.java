package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.mixin.EntityAccessor;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.vehicle.Boat;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class CompanionMountOnBoatBehavior extends ExtendedBehaviour<AbstractEntityCompanion> {

    public CompanionMountOnBoatBehavior(){
        cooldownFor((e)->20);
    }
    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(RegisterAI.NEAREST_BOAT.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel level, AbstractEntityCompanion entity) {
        LivingEntity owner = entity.getOwner();

        if(owner == null){
            return false;
        }
        else if(!((entity.getVehicle()) instanceof Boat)){
            return false;
        }

        Entity boat2ride = BrainUtils.getMemory(entity, RegisterAI.NEAREST_BOAT.get());

        return boat2ride != null && ((EntityAccessor) boat2ride).invokecanAddPassenger(entity);
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {

        Entity boat2ride = BrainUtils.getMemory(entity, RegisterAI.NEAREST_BOAT.get());

        if(boat2ride == null || !((EntityAccessor) boat2ride).invokecanAddPassenger(entity)){
            return;
        }

        if(entity.distanceTo(boat2ride)<3){
            entity.startRiding(boat2ride);
        }
        else {
            BehaviorUtils.setWalkAndLookTargetMemories(entity, boat2ride, 1, 0);
        }
    }
}

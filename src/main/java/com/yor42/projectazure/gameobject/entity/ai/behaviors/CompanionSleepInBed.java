package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;
import java.util.Optional;

public class CompanionSleepInBed extends ExtendedBehaviour<AbstractEntityCompanion> {

    public static final int COOLDOWN_AFTER_BEING_WOKEN = 100;
    private long nextOkStartTime;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.LAST_WOKEN, MemoryStatus.REGISTERED));
    }

    protected boolean checkExtraStartConditions(ServerLevel pLevel, AbstractEntityCompanion pOwner) {
        if (pOwner.isPassenger() || pLevel.isDay()) {
            return false;
        } else {
            Brain<?> brain = pOwner.getBrain();
            GlobalPos globalpos = brain.getMemory(MemoryModuleType.HOME).get();
            if (pLevel.dimension() != globalpos.dimension()) {
                return false;
            } else {
                Optional<Long> optional = brain.getMemory(MemoryModuleType.LAST_WOKEN);
                if (optional.isPresent()) {
                    long i = pLevel.getGameTime() - optional.get();
                    if (i > 0L && i < 100L) {
                        return false;
                    }
                }

                BlockState blockstate = pLevel.getBlockState(globalpos.pos());
                return globalpos.pos().closerToCenterThan(pOwner.position(), 3.0D) && blockstate.is(BlockTags.BEDS) && !blockstate.getValue(BedBlock.OCCUPIED);
            }
        }
    }

    protected boolean canStillUse(ServerLevel pLevel, AbstractEntityCompanion pEntity, long pGameTime) {
        Optional<GlobalPos> optional = pEntity.getBrain().getMemory(MemoryModuleType.HOME);
        if (!optional.isPresent()) {
            return false;
        } else {
            BlockPos blockpos = optional.get().pos();
            return pEntity.getBrain().isActive(Activity.REST) && pEntity.getY() > (double)blockpos.getY() + 0.4D && blockpos.closerToCenterThan(pEntity.position(), 1.14D);
        }
    }

    protected void start(AbstractEntityCompanion pEntity) {
        if (pEntity.level.getGameTime() > this.nextOkStartTime) {
            InteractWithDoor.closeDoorsThatIHaveOpenedOrPassedThrough((ServerLevel) pEntity.level, pEntity, (Node)null, (Node)null);
            pEntity.startSleeping(pEntity.getBrain().getMemory(MemoryModuleType.HOME).get().pos());
        }

    }

    protected boolean timedOut(long pGameTime) {
        return false;
    }
}

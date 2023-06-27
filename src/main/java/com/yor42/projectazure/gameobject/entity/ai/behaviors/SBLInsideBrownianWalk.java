package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SBLInsideBrownianWalk<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private final float speedModifier;

    public SBLInsideBrownianWalk(float pSpeedModifier){
        this.speedModifier = pSpeedModifier;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return !level.canSeeSky(entity.blockPosition());
    }

    @Override
    protected void start(E entity) {
        BlockPos blockpos = entity.blockPosition();
        Level level = entity.level;
        List<BlockPos> list = BlockPos.betweenClosedStream(blockpos.offset(-1, -1, -1), blockpos.offset(1, 1, 1)).map(BlockPos::immutable).collect(Collectors.toList());
        Collections.shuffle(list);
        Optional<BlockPos> optional = list.stream().filter((p_23230_) -> !level.canSeeSky(p_23230_)).filter((p_23237_) -> level.loadedAndEntityCanStandOn(p_23237_, entity)).filter((p_23227_) -> level.noCollision(entity)).findFirst();
        optional.ifPresent((p_23233_) -> entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(p_23233_, this.speedModifier, 0)));
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
    }
}

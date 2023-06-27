package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;
import java.util.Optional;

import static net.minecraft.world.entity.ai.memory.MemoryModuleType.HOME;

public class CompanionReturnHomeFromMemory extends ExtendedBehaviour<AbstractEntityCompanion> {

    private final float speedModifier;
    private final int closeEnoughDist;
    private final int tooFarDistance;
    private final int tooLongUnreachableDuration;

    public CompanionReturnHomeFromMemory(float pSpeedModifier, int pCloseEnoughDist, int pTooFarDistance, int pTooLongUnreachableDuration) {
        this.speedModifier = pSpeedModifier;
        this.closeEnoughDist = pCloseEnoughDist;
        this.tooFarDistance = pTooFarDistance;
        this.tooLongUnreachableDuration = pTooLongUnreachableDuration;
    }

    private void dropPOI(AbstractEntityCompanion pVillager, long pTime) {
        Brain<?> brain = pVillager.getBrain();
        pVillager.releasePoi(HOME);
        brain.eraseMemory(HOME);
        brain.setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, pTime);
    }

    @Override
    protected void start(ServerLevel pLevel, AbstractEntityCompanion pEntity, long pGameTime) {
        Brain<?> brain = pEntity.getBrain();
        brain.getMemory(HOME).ifPresent((p_24067_) -> {
            if (!this.wrongDimension(pLevel, p_24067_) && !this.tiredOfTryingToFindTarget(pLevel, pEntity)) {
                if (this.tooFar(pEntity, p_24067_)) {
                    Vec3 vec3 = null;
                    int i = 0;

                    for(int j = 1000; i < 1000 && (vec3 == null || this.tooFar(pEntity, GlobalPos.of(pLevel.dimension(), new BlockPos(vec3)))); ++i) {
                        vec3 = DefaultRandomPos.getPosTowards(pEntity, 15, 7, Vec3.atBottomCenterOf(p_24067_.pos()), (double)((float)Math.PI / 2F));
                    }

                    if (i == 1000) {
                        this.dropPOI(pEntity, pGameTime);
                        return;
                    }

                    brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3, this.speedModifier, this.closeEnoughDist));
                } else if (!this.closeEnough(pLevel, pEntity, p_24067_)) {
                    brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(p_24067_.pos(), this.speedModifier, this.closeEnoughDist));
                }
            } else {
                this.dropPOI(pEntity, pGameTime);
            }

        });
    }

    private boolean tiredOfTryingToFindTarget(ServerLevel pLevel, AbstractEntityCompanion pVillager) {
        Optional<Long> optional = pVillager.getBrain().getMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        return optional.filter(aLong -> pLevel.getGameTime() - aLong > (long) this.tooLongUnreachableDuration).isPresent();
    }

    private boolean tooFar(AbstractEntityCompanion pVillager, GlobalPos pMemoryType) {
        return pMemoryType.pos().distManhattan(pVillager.blockPosition()) > this.tooFarDistance;
    }

    private boolean wrongDimension(ServerLevel pLevel, GlobalPos pMemoryPos) {
        return pMemoryPos.dimension() != pLevel.dimension();
    }

    private boolean closeEnough(ServerLevel pLevel, AbstractEntityCompanion pVillager, GlobalPos pMemoryPos) {
        return pMemoryPos.dimension() == pLevel.dimension() && pMemoryPos.pos().distManhattan(pVillager.blockPosition()) <= this.closeEnoughDist;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED), Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), Pair.of(HOME, MemoryStatus.VALUE_PRESENT));
    }
}

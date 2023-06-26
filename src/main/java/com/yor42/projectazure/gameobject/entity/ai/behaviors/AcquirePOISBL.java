package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.AcquirePoi;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AcquirePOISBL extends ExtendedBehaviour<AbstractEntityCompanion> {

    private static final int BATCH_SIZE = 5;
    private static final int RATE = 20;
    public static final int SCAN_RANGE = 48;
    private final PoiType poiType;
    private final MemoryModuleType<GlobalPos> memoryToAcquire;
    private final boolean onlyIfAdult;
    private final Optional<Byte> onPoiAcquisitionEvent;
    private long nextScheduledStart;
    private final Long2ObjectMap<JitteredLinearRetry> batchCache = new Long2ObjectOpenHashMap<>();
    private final Pair<MemoryModuleType<?>, MemoryStatus> MemoryCondition;

    public AcquirePOISBL(PoiType pPoiType, MemoryModuleType<GlobalPos> pMemoryToAcquire, boolean pOnlyIfAdult, byte pOnPoiAcquistitionEvent){
        this.poiType = pPoiType;
        this.memoryToAcquire = pMemoryToAcquire;
        this.onlyIfAdult = pOnlyIfAdult;
        this.onPoiAcquisitionEvent = Optional.of(pOnPoiAcquistitionEvent);
        this.MemoryCondition = Pair.of(pMemoryToAcquire, MemoryStatus.VALUE_ABSENT);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(MemoryCondition);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {
        if (this.onlyIfAdult && entity.isBaby()) {
            return false;
        } else if (this.nextScheduledStart == 0L) {
            this.nextScheduledStart = entity.level.getGameTime() + (long)level.random.nextInt(20);
            return false;
        } else {
            return level.getGameTime() >= this.nextScheduledStart;
        }
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {
        ServerLevel pLevel = (ServerLevel) entity.level;
        long pGameTime = pLevel.getGameTime();
        this.nextScheduledStart = pGameTime + 20L + (long)pLevel.getRandom().nextInt(20);
        PoiManager poimanager = pLevel.getPoiManager();
        this.batchCache.long2ObjectEntrySet().removeIf((p_22338_) -> !p_22338_.getValue().isStillValid(pGameTime));
        Predicate<BlockPos> predicate = (p_22335_) -> {
            JitteredLinearRetry acquirepoi$jitteredlinearretry = this.batchCache.get(p_22335_.asLong());
            if (acquirepoi$jitteredlinearretry == null) {
                return true;
            } else if (!acquirepoi$jitteredlinearretry.shouldRetry(pGameTime)) {
                return false;
            } else {
                acquirepoi$jitteredlinearretry.markAttempt(pGameTime);
                return true;
            }
        };
        Set<BlockPos> set = poimanager.findAllClosestFirst(this.poiType.getPredicate(), predicate, entity.blockPosition(), 48, PoiManager.Occupancy.HAS_SPACE).limit(5L).collect(Collectors.toSet());
        Path path = entity.getNavigation().createPath(set, this.poiType.getValidRange());
        if (path != null && path.canReach()) {
            BlockPos blockpos1 = path.getTarget();
            poimanager.getType(blockpos1).ifPresent((p_22369_) -> {
                poimanager.take(this.poiType.getPredicate(), (p_147372_) -> p_147372_.equals(blockpos1), blockpos1, 1);
                entity.getBrain().setMemory(this.memoryToAcquire, GlobalPos.of(pLevel.dimension(), blockpos1));
                this.onPoiAcquisitionEvent.ifPresent((p_147369_) -> {
                    pLevel.broadcastEntityEvent(entity, p_147369_);
                });
                this.batchCache.clear();
                DebugPackets.sendPoiTicketCountPacket(pLevel, blockpos1);
            });
        } else {
            for(BlockPos blockpos : set) {
                this.batchCache.computeIfAbsent(blockpos.asLong(), (p_22360_) -> new JitteredLinearRetry(entity.level.random, pGameTime));
            }
        }

    }

    protected void start(ServerLevel pLevel, PathfinderMob pEntity, long pGameTime) {
        this.nextScheduledStart = pGameTime + 20L + (long)pLevel.getRandom().nextInt(20);
        PoiManager poimanager = pLevel.getPoiManager();
        this.batchCache.long2ObjectEntrySet().removeIf((p_22338_) -> !p_22338_.getValue().isStillValid(pGameTime));
        Predicate<BlockPos> predicate = (p_22335_) -> {
            JitteredLinearRetry acquirepoi$jitteredlinearretry = this.batchCache.get(p_22335_.asLong());
            if (acquirepoi$jitteredlinearretry == null) {
                return true;
            } else if (!acquirepoi$jitteredlinearretry.shouldRetry(pGameTime)) {
                return false;
            } else {
                acquirepoi$jitteredlinearretry.markAttempt(pGameTime);
                return true;
            }
        };
        Set<BlockPos> set = poimanager.findAllClosestFirst(this.poiType.getPredicate(), predicate, pEntity.blockPosition(), 48, PoiManager.Occupancy.HAS_SPACE).limit(5L).collect(Collectors.toSet());
        Path path = pEntity.getNavigation().createPath(set, this.poiType.getValidRange());
        if (path != null && path.canReach()) {
            BlockPos blockpos1 = path.getTarget();
            poimanager.getType(blockpos1).ifPresent((p_22369_) -> {
                poimanager.take(this.poiType.getPredicate(), (p_147372_) -> p_147372_.equals(blockpos1), blockpos1, 1);
                pEntity.getBrain().setMemory(this.memoryToAcquire, GlobalPos.of(pLevel.dimension(), blockpos1));
                this.onPoiAcquisitionEvent.ifPresent((p_147369_) -> {
                    pLevel.broadcastEntityEvent(pEntity, p_147369_);
                });
                this.batchCache.clear();
                DebugPackets.sendPoiTicketCountPacket(pLevel, blockpos1);
            });
        } else {
            for(BlockPos blockpos : set) {
                this.batchCache.computeIfAbsent(blockpos.asLong(), (p_22360_) -> new JitteredLinearRetry(pEntity.level.random, pGameTime));
            }
        }

    }

    static class JitteredLinearRetry {
        private static final int MIN_INTERVAL_INCREASE = 40;
        private static final int MAX_INTERVAL_INCREASE = 80;
        private static final int MAX_RETRY_PATHFINDING_INTERVAL = 400;
        private final Random random;
        private long previousAttemptTimestamp;
        private long nextScheduledAttemptTimestamp;
        private int currentDelay;

        JitteredLinearRetry(Random pRandom, long pTimestamp) {
            this.random = pRandom;
            this.markAttempt(pTimestamp);
        }

        public void markAttempt(long pTimestamp) {
            this.previousAttemptTimestamp = pTimestamp;
            int i = this.currentDelay + this.random.nextInt(40) + 40;
            this.currentDelay = Math.min(i, 400);
            this.nextScheduledAttemptTimestamp = pTimestamp + (long)this.currentDelay;
        }

        public boolean isStillValid(long pTimestamp) {
            return pTimestamp - this.previousAttemptTimestamp < 400L;
        }

        public boolean shouldRetry(long pTimestamp) {
            return pTimestamp >= this.nextScheduledAttemptTimestamp;
        }

        public String toString() {
            return "RetryMarker{, previousAttemptAt=" + this.previousAttemptTimestamp + ", nextScheduledAttemptAt=" + this.nextScheduledAttemptTimestamp + ", currentDelay=" + this.currentDelay + "}";
        }
    }
}

package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.lwjgl.system.CallbackI;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SBLSetClosestHomeAsWalkTarget<E extends LivingEntity> extends ExtendedBehaviour<E> {
    private final float speedModifier;
    private final Long2LongMap batchCache = new Long2LongOpenHashMap();
    private int triedCount;
    private long lastUpdate;


    public SBLSetClosestHomeAsWalkTarget(float SpeedModifier){
        this.speedModifier = SpeedModifier;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        if (level.getGameTime() - this.lastUpdate < 20L) {
            return false;
        } else {
            PathfinderMob pathfindermob = (PathfinderMob)entity;
            PoiManager poimanager = level.getPoiManager();
            Optional<BlockPos> optional = poimanager.findClosest(PoiType.HOME.getPredicate(), entity.blockPosition(), 48, PoiManager.Occupancy.ANY);
            return optional.isPresent() && !(optional.get().distSqr(pathfindermob.blockPosition()) <= 4.0D);
        }
    }

    @Override
    protected void start(E entity) {
        ServerLevel level = (ServerLevel) entity.level;
        this.triedCount = 0;
        this.lastUpdate = level.getGameTime() + (long)level.getRandom().nextInt(20);
        PathfinderMob pathfindermob = (PathfinderMob)entity;
        PoiManager poimanager = level.getPoiManager();
        Predicate<BlockPos> predicate = (p_23886_) -> {
            long i = p_23886_.asLong();
            if (this.batchCache.containsKey(i)) {
                return false;
            } else if (++this.triedCount >= 5) {
                return false;
            } else {
                this.batchCache.put(i, this.lastUpdate + 40L);
                return true;
            }
        };
        Stream<BlockPos> stream = poimanager.findAll(PoiType.HOME.getPredicate(), predicate, entity.blockPosition(), 48, PoiManager.Occupancy.ANY);
        Path path = pathfindermob.getNavigation().createPath(stream, PoiType.HOME.getValidRange());
        if (path != null && path.canReach()) {
            BlockPos blockpos = path.getTarget();
            Optional<PoiType> optional = poimanager.getType(blockpos);
            if (optional.isPresent()) {
                entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(blockpos, this.speedModifier, 1));
                DebugPackets.sendPoiTicketCountPacket(level, blockpos);
            }
        } else if (this.triedCount < 5) {
            this.batchCache.long2LongEntrySet().removeIf((p_23888_) -> p_23888_.getLongValue() < this.lastUpdate);
        }
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), Pair.of(MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT));
    }
}

package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Predicate;

public class CompanionValidateHome extends ExtendedBehaviour<AbstractEntityCompanion> {

    private static final int MAX_DISTANCE = 16;
    private final MemoryModuleType<GlobalPos> memoryType;
    private final Predicate<PoiType> poiPredicate;

    public CompanionValidateHome() {
        this.poiPredicate = PoiType.HOME.getPredicate();
        this.memoryType = MemoryModuleType.HOME;
    }

    protected boolean checkExtraStartConditions(ServerLevel pLevel, AbstractEntityCompanion pOwner) {
        GlobalPos globalpos = pOwner.getBrain().getMemory(this.memoryType).get();
        return pLevel.dimension() == globalpos.dimension() && globalpos.pos().closerToCenterThan(pOwner.position(), 16.0D);
    }


    @Override
    protected void start(AbstractEntityCompanion entity) {
        GlobalPos globalpos = BrainUtils.getMemory(entity, MemoryModuleType.HOME);
        BlockPos blockpos = globalpos.pos();
        ServerLevel serverlevel = ((ServerLevel)entity.level).getServer().getLevel(globalpos.dimension());
        if (serverlevel != null && !this.poiDoesntExist(serverlevel, blockpos)) {
            if (this.bedIsOccupied(serverlevel, blockpos, entity)) {
                BrainUtils.clearMemory(entity, MemoryModuleType.HOME);
                ((ServerLevel)entity.level).getPoiManager().release(blockpos);
                DebugPackets.sendPoiTicketCountPacket(((ServerLevel)entity.level), blockpos);
            }
        } else {
            BrainUtils.clearMemory(entity, MemoryModuleType.HOME);
        }
    }
    private boolean bedIsOccupied(ServerLevel pLevel, BlockPos pPos, LivingEntity pSleeper) {
        BlockState blockstate = pLevel.getBlockState(pPos);
        return blockstate.is(BlockTags.BEDS) && blockstate.getValue(BedBlock.OCCUPIED) && !pSleeper.isSleeping();
    }

    private boolean poiDoesntExist(ServerLevel pLevel, BlockPos pPos) {
        return !pLevel.getPoiManager().exists(pPos, this.poiPredicate);
    }
    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.HOME,MemoryStatus.VALUE_PRESENT));
    }
}

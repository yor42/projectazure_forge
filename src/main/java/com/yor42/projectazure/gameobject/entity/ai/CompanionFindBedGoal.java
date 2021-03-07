package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class CompanionFindBedGoal extends Goal {

    AbstractEntityCompanion entityCompanion;

    public CompanionFindBedGoal(AbstractEntityCompanion companion){
        this.entityCompanion = companion;
    }

    @Override
    public boolean shouldExecute() {

        return !this.entityCompanion.getBrain().hasMemory(MemoryModuleType.HOME) && this.entityCompanion.ticksExisted % 300 == 0;
    }

    @Override
    public void startExecuting() {
        if(this.entityCompanion.isServerWorld()) {
            Optional<BlockPos> optional = findHomePosition((ServerWorld) this.entityCompanion.getEntityWorld(), this.entityCompanion);
            optional.ifPresent(blockPos -> setHomePosition((ServerWorld) this.entityCompanion.getEntityWorld(), this.entityCompanion, blockPos));
        }
    }

    private Optional<BlockPos> findHomePosition(ServerWorld world, AbstractEntityCompanion entity) {
        return world.getPointOfInterestManager().take(PointOfInterestType.HOME.getPredicate(), (pos) -> this.canReachHomePosition(entity, pos), entity.getPosition(), 48);
    }

    private boolean canReachHomePosition(AbstractEntityCompanion entity, BlockPos pos) {
        Path path = entity.getNavigator().getPathToPos(pos, PointOfInterestType.HOME.getValidRange());
        return path != null && path.reachesTarget();
    }

    private void setHomePosition(ServerWorld world, AbstractEntityCompanion entityCompanion, BlockPos pos) {
        GlobalPos globalpos = GlobalPos.getPosition(world.getDimensionKey(), pos);
        entityCompanion.setBedPosition(pos);
        entityCompanion.getBrain().setMemory(MemoryModuleType.HOME, globalpos);
    }
}

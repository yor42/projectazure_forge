package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
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

        boolean alreadyhasbed = this.entityCompanion.getHOMEPOS().isPresent();

        return !alreadyhasbed && this.entityCompanion.ticksExisted % 10 == 0;
    }

    @Override
    public void startExecuting() {
            Optional<BlockPos> optional = findHomePosition((ServerWorld) this.entityCompanion.getEntityWorld(), this.entityCompanion);
            optional.ifPresent(blockPos -> setHomePosition(this.entityCompanion, blockPos));
    }

    private Optional<BlockPos> findHomePosition(ServerWorld world, AbstractEntityCompanion entity) {
        return world.getPointOfInterestManager().take(PointOfInterestType.HOME.getPredicate(), (pos) -> this.canReachHomePosition(entity, pos), entity.getPosition(), 48);
    }

    private boolean canReachHomePosition(AbstractEntityCompanion entity, BlockPos pos) {
        Path path = entity.getNavigator().getPathToPos(pos, PointOfInterestType.HOME.getValidRange());
        return path != null && path.reachesTarget();
    }

    private void setHomePosition(AbstractEntityCompanion entityCompanion, BlockPos pos) {
        entityCompanion.setHomeposAndDistance(pos,64);
    }
}

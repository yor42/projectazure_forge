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
    public boolean canUse() {

        boolean alreadyhasbed = this.entityCompanion.getHOMEPOS().isPresent();

        return !alreadyhasbed && this.entityCompanion.tickCount % 10 == 0;
    }

    @Override
    public void start() {
            Optional<BlockPos> optional = findHomePosition((ServerWorld) this.entityCompanion.getCommandSenderWorld(), this.entityCompanion);
            optional.ifPresent(blockPos -> setHomePosition(this.entityCompanion, blockPos));
    }

    private Optional<BlockPos> findHomePosition(ServerWorld world, AbstractEntityCompanion entity) {
        return world.getPoiManager().take(PointOfInterestType.HOME.getPredicate(), (pos) -> this.canReachHomePosition(entity, pos), entity.blockPosition(), 48);
    }

    private boolean canReachHomePosition(AbstractEntityCompanion entity, BlockPos pos) {
        Path path = entity.getNavigation().createPath(pos, PointOfInterestType.HOME.getValidRange());
        return path != null && path.canReach();
    }

    private void setHomePosition(AbstractEntityCompanion entityCompanion, BlockPos pos) {
        entityCompanion.setHomeposAndDistance(pos,64);
    }
}

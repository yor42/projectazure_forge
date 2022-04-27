package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.pathfinder.Path;

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
            Optional<BlockPos> optional = findHomePosition((ServerLevel) this.entityCompanion.getCommandSenderWorld(), this.entityCompanion);
            optional.ifPresent(blockPos -> setHomePosition(this.entityCompanion, blockPos));
    }

    private Optional<BlockPos> findHomePosition(ServerLevel world, AbstractEntityCompanion entity) {
        return world.getPoiManager().take(PoiType.HOME.getPredicate(), (pos) -> this.canReachHomePosition(entity, pos), entity.blockPosition(), 48);
    }

    private boolean canReachHomePosition(AbstractEntityCompanion entity, BlockPos pos) {
        Path path = entity.getNavigation().createPath(pos, PoiType.HOME.getValidRange());
        return path != null && path.canReach();
    }

    private void setHomePosition(AbstractEntityCompanion entityCompanion, BlockPos pos) {
        entityCompanion.setHomeposAndDistance(pos,64);
    }
}

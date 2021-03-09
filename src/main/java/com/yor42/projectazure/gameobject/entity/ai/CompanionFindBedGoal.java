package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.server.ServerWorld;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompanionFindBedGoal extends Goal {

    AbstractEntityCompanion entityCompanion;

    public CompanionFindBedGoal(AbstractEntityCompanion companion){
        this.entityCompanion = companion;
    }

    @Override
    public boolean shouldExecute() {

        boolean notalreadyhasbed = (this.entityCompanion.getHomePosition()==BlockPos.ZERO);

        boolean flag = notalreadyhasbed && this.entityCompanion.ticksExisted % 10 == 0;

        return flag;
    }

    @Override
    public void startExecuting() {
            Optional<BlockPos> optional = findHomePosition((ServerWorld) this.entityCompanion.getEntityWorld(), this.entityCompanion);
            optional.ifPresent(blockPos -> setHomePosition((ServerWorld) this.entityCompanion.getEntityWorld(), this.entityCompanion, blockPos));
    }

    private Optional<BlockPos> findHomePosition(ServerWorld world, AbstractEntityCompanion entity) {
        return world.getPointOfInterestManager().take(PointOfInterestType.HOME.getPredicate(), (pos) -> this.canReachHomePosition(entity, pos), entity.getPosition(), 48);
    }

    private boolean canReachHomePosition(AbstractEntityCompanion entity, BlockPos pos) {
        Path path = entity.getNavigator().getPathToPos(pos, PointOfInterestType.HOME.getValidRange());
        return path != null && path.reachesTarget();
    }

    private List<BlockPos> getNearbyFreeBed() {
        BlockPos blockpos = this.entityCompanion.getPosition();
        PointOfInterestManager pointofinterestmanager = ((ServerWorld)this.entityCompanion.getEntityWorld()).getPointOfInterestManager();
        Stream<PointOfInterest> stream = pointofinterestmanager.func_219146_b((p_226486_0_) -> p_226486_0_ == PointOfInterestType.HOME, blockpos, 20, PointOfInterestManager.Status.ANY);
        return stream.map(PointOfInterest::getPos).filter(this::isBedFree).sorted(Comparator.comparingDouble((p_226488_1_) -> p_226488_1_.distanceSq(blockpos))).collect(Collectors.toList());
    }

    private boolean isBedFree(BlockPos pos) {
        BlockState blockstate = this.entityCompanion.getEntityWorld().getBlockState(pos);
        if (blockstate.isBed(this.entityCompanion.getEntityWorld(), pos, null)) {
            return !blockstate.get(BedBlock.OCCUPIED);
        }
        return false;
    }

    private void setHomePosition(ServerWorld world, AbstractEntityCompanion entityCompanion, BlockPos pos) {
        GlobalPos globalpos = GlobalPos.getPosition(world.getDimensionKey(), pos);
        entityCompanion.setHomePosAndDistance(pos,32);
        entityCompanion.getBrain().setMemory(MemoryModuleType.HOME, globalpos);
    }
}

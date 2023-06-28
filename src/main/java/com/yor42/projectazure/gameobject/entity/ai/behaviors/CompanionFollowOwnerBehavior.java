package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowOwner;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;

public class CompanionFollowOwnerBehavior extends FollowOwner<AbstractEntityCompanion> {

    @Override
    protected boolean shouldKeepRunning(AbstractEntityCompanion entity) {

        LivingEntity target = this.followingEntityProvider.apply(entity);

        if (target == null)
            return false;

        double dist = entity.distanceToSqr(target);
        double minDist = this.followDistMin.apply(entity, target);

        return dist > minDist * minDist;
    }
    @Override
    protected void teleportToTarget(AbstractEntityCompanion entity, LivingEntity target) {
        Level level = entity.level;
        BlockPos entityPos = target.blockPosition();

        BlockPos pos = getRandomPositionWithinRange(entityPos, 5, 5, 5, 1, 1, 1, true, level, 10, (state, statePos) -> {
            BlockPathTypes pathTypes = entity.getNavigation().getNodeEvaluator().getBlockPathType(level, statePos.getX(), statePos.getY(), statePos.getZ());

            if (pathTypes != BlockPathTypes.WALKABLE)
                return false;

            return level.noCollision(entity, entity.getBoundingBox().move(statePos.subtract(entityPos)));
        });

        if (pos != entityPos) {
            entity.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, entity.getYRot(), entity.getXRot());
            entity.getNavigation().stop();
            BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
        }
    }

    private static BlockPos getRandomPositionWithinRange(BlockPos centerPos, int xRadius, int yRadius, int zRadius, int minSpreadX, int minSpreadY, int minSpreadZ, boolean safeSurfacePlacement, Level world, int tries, @Nullable BiPredicate<BlockState, BlockPos> statePredicate) {
        BlockPos.MutableBlockPos mutablePos = centerPos.mutable();
        xRadius = Math.max(xRadius - minSpreadX, 0);
        yRadius = Math.max(yRadius - minSpreadY, 0);
        zRadius = Math.max(zRadius - minSpreadZ, 0);

        for (int i = 0; i < tries; i++) {
            double xAdjust = MathUtil.getRand().nextFloat() * xRadius * 2 - xRadius;
            double yAdjust = MathUtil.getRand().nextFloat() * yRadius * 2 - yRadius;
            double zAdjust = MathUtil.getRand().nextFloat() * zRadius * 2 - zRadius;
            int newX = (int)Math.floor(centerPos.getX() + xAdjust + minSpreadX * Math.signum(xAdjust));
            int newY = (int)Math.floor(centerPos.getY() + yAdjust + minSpreadY * Math.signum(yAdjust));
            int newZ = (int)Math.floor(centerPos.getZ() + zAdjust + minSpreadZ * Math.signum(zAdjust));

            mutablePos.set(newX, newY, newZ);

            if (safeSurfacePlacement && world != null)
                mutablePos.set(world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mutablePos));

            if (statePredicate == null || statePredicate.test(world.getBlockState(mutablePos), mutablePos.immutable()))
                return mutablePos.immutable();
        }

        return centerPos;
    }
}

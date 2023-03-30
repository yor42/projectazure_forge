package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.network.packets.spawnParticlePacket;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.Random;

import static com.yor42.projectazure.setup.register.RegisterAI.NEAREST_ORE;

public class FollowOwnerTask extends Behavior<AbstractEntityCompanion> {
    public FollowOwnerTask() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
    }

    private static final UniformInt followrange = TimeUtil.rangeOfSeconds(1,2);

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel world, AbstractEntityCompanion entity) {
        LivingEntity owner = entity.getOwner();
        return owner!=null && !owner.onClimbable() && entity.distanceTo(owner)>=(entity.getBrain().hasMemoryValue(NEAREST_ORE.get()) || entity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)|| entity.getBrain().hasMemoryValue(RegisterAI.NEAREST_WORLDSKILLABLE.get())? 16:6);
    }

    protected void start(@Nonnull ServerLevel p_212831_1_, @Nonnull AbstractEntityCompanion entity, long p_212831_3_) {
        if (entity.getOwner()!=null) {
            if(entity.distanceTo(entity.getOwner())>32){
                this.tryToTeleportNearEntity(entity, entity.getOwner());
            }
            BehaviorUtils.setWalkAndLookTargetMemories(entity, entity.getOwner(), 1, followrange.sample(new Random()));
        }
    }

    private void tryToTeleportNearEntity(AbstractEntityCompanion ety, LivingEntity target) {
        BlockPos blockpos = target.blockPosition();

        for(int i = 0; i < 10; ++i) {
            int j = this.getRandomNumber(-3, 3);
            int k = this.getRandomNumber(-1, 1);
            int l = this.getRandomNumber(-3, 3);
            boolean flag = this.tryToTeleportToLocation(ety, target, blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if (flag) {
                return;
            }
        }

    }

    private int getRandomNumber(int min, int max) {
        return MathUtil.getRand().nextInt(max - min + 1) + min;
    }

    private boolean tryToTeleportToLocation(AbstractEntityCompanion ety, LivingEntity tgt, int x, int y, int z) {
        if (Math.abs((double)x - tgt.getX()) < 2.0D && Math.abs((double)z - tgt.getZ()) < 2.0D) {
            return false;
        } else if (!this.isTeleportFriendlyBlock(ety, new BlockPos(x, y, z))) {
            return false;
        } else {
            ety.moveTo((double)x + 0.5D, y, (double)z + 0.5D, ety.getYRot(), ety.getXRot());
            ety.getNavigation().stop();
            Main.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> ety), new spawnParticlePacket(ety, spawnParticlePacket.Particles.TELEPORT));
            return true;
        }
    }

    private boolean isTeleportFriendlyBlock(AbstractEntityCompanion ety, BlockPos pos) {
        BlockPathTypes pathnodetype = WalkNodeEvaluator.getBlockPathTypeStatic(ety.getLevel(), pos.mutable());
        if (pathnodetype != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = ety.getLevel().getBlockState(pos.below());
            if (blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pos.subtract(ety.blockPosition());
                return ety.getLevel().noCollision(ety, ety.getBoundingBox().move(blockpos));
            }
        }
    }
}

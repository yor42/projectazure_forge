package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.network.packets.spawnParticlePacket;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;

import static com.yor42.projectazure.setup.register.registerManager.NEAREST_ORE;
import static net.minecraftforge.fml.network.PacketDistributor.TRACKING_ENTITY;

public class FollowOwnerTask extends Task<AbstractEntityCompanion> {
    public FollowOwnerTask() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT));
    }

    private static final RangedInteger followrange = new RangedInteger(1,4);

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerWorld world, AbstractEntityCompanion entity) {
        LivingEntity owner = entity.getOwner();
        return owner!=null && !owner.onClimbable() && entity.distanceTo(owner)>=(entity.getBrain().hasMemoryValue(NEAREST_ORE.get()) || entity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)|| entity.getBrain().hasMemoryValue(registerManager.NEAREST_WORLDSKILLABLE.get())? 16:6);
    }

    protected void start(@Nonnull ServerWorld p_212831_1_, @Nonnull AbstractEntityCompanion entity, long p_212831_3_) {
        if (entity.getOwner()!=null) {
            if(entity.distanceTo(entity.getOwner())>32){
                this.tryToTeleportNearEntity(entity, entity.getOwner());
            }
            BrainUtil.setWalkAndLookTargetMemories(entity, entity.getOwner(), 1, followrange.randomValue(new Random()));
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
            ety.moveTo((double)x + 0.5D, (double)y, (double)z + 0.5D, ety.yRot, ety.xRot);
            ety.getNavigation().stop();
            Main.NETWORK.send(TRACKING_ENTITY.with(() -> ety), new spawnParticlePacket(ety, spawnParticlePacket.Particles.TELEPORT));
            return true;
        }
    }

    private boolean isTeleportFriendlyBlock(AbstractEntityCompanion ety, BlockPos pos) {
        PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic(ety.getCommandSenderWorld(), pos.mutable());
        if (pathnodetype != PathNodeType.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = ety.getCommandSenderWorld().getBlockState(pos.below());
            if (blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pos.subtract(ety.blockPosition());
                return ety.getCommandSenderWorld().noCollision(ety, ety.getBoundingBox().move(blockpos));
            }
        }
    }
}

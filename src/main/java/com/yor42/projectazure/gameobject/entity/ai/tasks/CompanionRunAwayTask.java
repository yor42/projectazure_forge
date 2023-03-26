package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

import static com.yor42.projectazure.setup.register.RegisterAI.HURT_AT;

public class CompanionRunAwayTask extends Behavior<AbstractEntityCompanion> {
    BlockPos pos;

    public CompanionRunAwayTask() {
        super(ImmutableMap.of(HURT_AT.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, AbstractEntityCompanion entity) {

        if (entity.isOnFire()) {
            BlockPos blockpos = this.lookForWater(entity.level, entity, 5, 4);
            if (blockpos != null) {
                this.pos = blockpos;
                return true;
            }
        }

        return this.findRandomPosition(entity);
    }

    @Override
    protected void start(ServerLevel p_212831_1_, AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        WalkTarget walktarget = new WalkTarget(new BlockPosTracker(this.pos), 1.2F, 1);
        p_212831_2_.getBrain().setMemory(MemoryModuleType.WALK_TARGET, walktarget);
        p_212831_2_.getBrain().eraseMemory(HURT_AT.get());
    }

    @Nullable
    protected BlockPos lookForWater(BlockGetter p_188497_1_, Entity p_188497_2_, int p_188497_3_, int p_188497_4_) {
        BlockPos blockpos = p_188497_2_.blockPosition();
        int i = blockpos.getX();
        int j = blockpos.getY();
        int k = blockpos.getZ();
        float f = (float)(p_188497_3_ * p_188497_3_ * p_188497_4_ * 2);
        BlockPos blockpos1 = null;
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();

        for(int l = i - p_188497_3_; l <= i + p_188497_3_; ++l) {
            for(int i1 = j - p_188497_4_; i1 <= j + p_188497_4_; ++i1) {
                for(int j1 = k - p_188497_3_; j1 <= k + p_188497_3_; ++j1) {
                    blockpos$mutable.set(l, i1, j1);
                    if (p_188497_1_.getFluidState(blockpos$mutable).is(FluidTags.WATER)) {
                        float f1 = (float)((l - i) * (l - i) + (i1 - j) * (i1 - j) + (j1 - k) * (j1 - k));
                        if (f1 < f) {
                            f = f1;
                            blockpos1 = new BlockPos(blockpos$mutable);
                        }
                    }
                }
            }
        }

        return blockpos1;
    }

    protected boolean findRandomPosition(AbstractEntityCompanion entity) {
        Vec3 vector3d = RandomPos.getPos(entity, 5, 4);
        if (vector3d == null) {
            return false;
        } else {
            this.pos = new BlockPos(vector3d);
            return true;
        }
    }
}

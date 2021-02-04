package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.EnumSet;

abstract class KansenMoveEntityForWorkGoal extends Goal {

    protected EntityKansenBase host;
    private final double speed;
    protected BlockPos targetBlockPos;
    protected boolean work;

    protected KansenMoveEntityForWorkGoal(EntityKansenBase entity, double speed) {
        this.host = entity;
        this.speed = speed;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    public boolean shouldExecute()
    {
        if(this.targetBlockPos != null)
        {
            return false;
        }
        return this.searchForDestination();
    }

    private boolean searchForDestination()
    {
        BlockPos blockpos = this.host.getPosition();

        for(int l = 0; l < 16; ++l)
        {
            for(int k = 0; k <= l; k = k > 0 ? -k : 1 - k)
            {
                for(int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1)
                {
                    for(int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1)
                    {
                        BlockPos blockpos1 = blockpos.add(i1, k, j1);

                        if(this.shouldMoveTo(this.host.world, blockpos1))
                        {
                            this.targetBlockPos = blockpos1;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void resetTask() {
        BlockPos blockpos = this.targetBlockPos.up();
        this.host.getNavigator().clearPath();
        this.host.world.sendBlockBreakProgress(this.host.getEntityId(), blockpos, -1);
        this.targetBlockPos = null;
        super.resetTask();
    }

    public void tick()
    {
        BlockPos blockpos = this.targetBlockPos.up();
        if(blockpos.distanceSq(this.host.getPosX(), this.host.getPosY(), this.host.getPosZ(), false) <= 2d)
        {
            this.work = true;
        }
        else
        {
            this.work = false;
            this.host.getNavigator().tryMoveToXYZ((double)((float)blockpos.getX()) + 0.5D, (double)blockpos.getY(), (double)((float)blockpos.getZ()) + 0.5D, this.speed);
            if(!this.host.getNavigator().tryMoveToXYZ((double)((float)blockpos.getX()) + 0.5D, (double)blockpos.getY(), (double)((float)blockpos.getZ()) + 0.5D, this.speed))
            {
                this.work = true;
            }
        }
    }

    abstract boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos);
}

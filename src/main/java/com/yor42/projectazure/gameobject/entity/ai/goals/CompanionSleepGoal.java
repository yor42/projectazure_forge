package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.BedBlock;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class CompanionSleepGoal extends Goal {

    private final AbstractEntityCompanion companion;

    public CompanionSleepGoal(AbstractEntityCompanion companion){
        this.companion = companion;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        boolean flag = this.companion.getHOMEPOS().isPresent() && this.companion.getEntityWorld().getDimensionKey() == World.OVERWORLD;
        if(flag){
            if(this.companion.getEntityWorld().isNightTime() && !this.companion.isForceWaken()){

                boolean flag3 = this.companion.isInHomeRangefromCurrenPos();
                boolean flag4 = (this.companion.isFreeRoaming()||(this.companion.getOwner()!=null && this.companion.getOwner().isSleeping()) || this.companion.getMorale()<=30) || (this.companion.isFreeRoaming() && (this.companion.getEntityWorld().isThundering() || this.companion.getEntityWorld().isNightTime()));
                boolean flag2 = !this.companion.isSleeping();
                BlockPos pos = this.companion.getHOMEPOS().get();
                boolean flag5 = this.companion.getEntityWorld().getBlockState(pos).isBed(this.companion.getEntityWorld(), pos, this.companion) && !this.companion.getEntityWorld().getBlockState(pos).get(BedBlock.OCCUPIED);
                return flag2 && flag3 && flag4 && flag5;
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if(this.companion.getEntityWorld().isNightTime()){
            return this.companion.isInHomeRangefromCurrenPos() && !this.companion.isSleeping();
        }
        return false;
    }

    @Override
    public void startExecuting() {
        if(this.companion.getHOMEPOS().isPresent()) {
            BlockPos pos = this.companion.getHOMEPOS().get();
            if (this.companion.getNavigator().getPath() != null)
                this.companion.getNavigator().clearPath();

            this.companion.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1);
            super.startExecuting();
        }
    }

    @Override
    public void tick() {
        if(this.companion.getHOMEPOS().isPresent()) {
            BlockPos pos = this.companion.getHOMEPOS().get();
            if (this.companion.ticksExisted % 20 == 0 && !this.companion.isSleeping()) {
                this.companion.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1);
            }

            if (this.companion.getDistanceSq(Vector3d.copyCentered(pos)) < 3) {
                if (!this.companion.isSleeping()) {
                    this.companion.startSleeping(pos);
                }
            }

            super.tick();
        }
    }
}

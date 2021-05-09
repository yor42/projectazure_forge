package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class CompanionSleepGoal extends Goal {

    private final AbstractEntityCompanion companion;

    public CompanionSleepGoal(AbstractEntityCompanion companion){
        this.companion = companion;
    }

    @Override
    public boolean shouldExecute() {
        boolean flag = this.companion.getHomePos() != BlockPos.ZERO && this.companion.getEntityWorld().getDimensionKey() == World.OVERWORLD;;
        if(flag){
            if(this.companion.getEntityWorld().isNightTime() && !this.companion.isForceWaken()){
                return this.companion.isInHomeRangefromCurrenPos() && !this.companion.isSleeping() && (this.companion.isFreeRoaming()||(this.companion.getOwner()!=null && this.companion.getOwner().isSleeping()));
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
        BlockPos pos = this.companion.getHomePos();
        if(this.companion.getNavigator().getPath() != null)
            this.companion.getNavigator().clearPath();

        this.companion.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1);
        super.startExecuting();
    }

    @Override
    public void tick() {
        BlockPos pos = this.companion.getHomePos();
        if(this.companion.ticksExisted%20==0 && !this.companion.isSleeping()){
            this.companion.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1);
        }

        if(this.companion.getDistanceSq(Vector3d.copyCentered(pos))<1){
            if(!this.companion.isSleeping()) {
                this.companion.startSleeping(pos);
            }
        }

        super.tick();
    }
}

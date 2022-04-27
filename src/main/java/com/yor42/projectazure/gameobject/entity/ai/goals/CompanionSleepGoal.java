package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class CompanionSleepGoal extends Goal {

    private final AbstractEntityCompanion companion;

    public CompanionSleepGoal(AbstractEntityCompanion companion){
        this.companion = companion;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        boolean flag = this.companion.getHOMEPOS().isPresent() && this.companion.getCommandSenderWorld().dimension() == Level.OVERWORLD;
        if(flag){
            if(this.companion.getCommandSenderWorld().isNight() && !this.companion.isForceWaken()){

                boolean flag3 = this.companion.isInHomeRangefromCurrenPos();
                boolean flag4 = (this.companion.isFreeRoaming()||(this.companion.getOwner()!=null && this.companion.getOwner().isSleeping()) || this.companion.getMorale()<=30) || (this.companion.isFreeRoaming() && (this.companion.getCommandSenderWorld().isThundering() || this.companion.getCommandSenderWorld().isNight()));
                boolean flag2 = !this.companion.isSleeping();
                BlockPos pos = this.companion.getHOMEPOS().get();
                boolean flag5 = this.companion.getCommandSenderWorld().getBlockState(pos).isBed(this.companion.getCommandSenderWorld(), pos, this.companion) && !this.companion.getCommandSenderWorld().getBlockState(pos).getValue(BedBlock.OCCUPIED);
                return flag2 && flag3 && flag4 && flag5;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if(this.companion.getCommandSenderWorld().isNight()){
            return this.companion.isInHomeRangefromCurrenPos() && !this.companion.isSleeping();
        }
        return false;
    }

    @Override
    public void start() {
        if(this.companion.getHOMEPOS().isPresent()) {
            BlockPos pos = this.companion.getHOMEPOS().get();
            if (this.companion.getNavigation().getPath() != null)
                this.companion.getNavigation().stop();

            this.companion.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1);
            super.start();
        }
    }

    @Override
    public void tick() {
        if(this.companion.getHOMEPOS().isPresent()) {
            BlockPos pos = this.companion.getHOMEPOS().get();
            if (this.companion.tickCount % 20 == 0 && !this.companion.isSleeping()) {
                this.companion.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1);
            }

            if (this.companion.distanceToSqr(Vec3.atCenterOf(pos)) < 3) {
                if (!this.companion.isSleeping()) {
                    this.companion.startSleeping(pos);
                }
            }

            super.tick();
        }
    }
}

package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class CompanionMoveToRecruitStationGoal extends Goal {

    private final AbstractEntityCompanion host;
    Vector3d NextPos;

    public CompanionMoveToRecruitStationGoal(AbstractEntityCompanion companion){
        this.host = companion;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        this.NextPos = this.getPosition();
        return this.host.isMovingtoRecruitStation && this.host.getRecruitStationPos().isPresent() && this.NextPos != null;
    }

    @Override
    public void tick() {
        if(this.NextPos == null || this.host.getDistanceSq(this.NextPos)<=9){
            this.NextPos = this.getPosition();
            this.host.getNavigator().clearPath();
        }

        if(this.NextPos != null) {
            this.host.getNavigator().tryMoveToXYZ(this.NextPos.getX(), this.NextPos.getY(), this.NextPos.getZ(), 1.0);
        }

        if(this.host.getRecruitStationPos().isPresent() && this.host.getDistanceSq(this.host.getRecruitStationPos().get().getX(),this.host.getRecruitStationPos().get().getY(),this.host.getRecruitStationPos().get().getZ())<=9){
            this.host.stopMovingtoRecruitStation();
        }

    }

    @Nullable
    protected Vector3d getPosition() {
        if(this.host.getRecruitStationPos().isPresent()) {
            return RandomPositionGenerator.findRandomTargetBlockTowards(this.host, 10, 7, Vector3d.copyCentered(this.host.getRecruitStationPos().get()));
        }
        else{
            return null;
        }
    }
}
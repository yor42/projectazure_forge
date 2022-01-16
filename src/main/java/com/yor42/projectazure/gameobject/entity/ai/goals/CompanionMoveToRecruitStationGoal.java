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
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        this.NextPos = this.getPosition();
        return this.host.isMovingtoRecruitStation && this.host.getRecruitStationPos().isPresent() && this.NextPos != null;
    }

    @Override
    public void tick() {
        if(this.NextPos == null || this.host.distanceToSqr(this.NextPos)<=9){
            this.NextPos = this.getPosition();
            this.host.getNavigation().stop();
        }

        if(this.NextPos != null) {
            this.host.getNavigation().moveTo(this.NextPos.x(), this.NextPos.y(), this.NextPos.z(), 1.0);
        }

        if(this.host.getRecruitStationPos().isPresent() && this.host.distanceToSqr(this.host.getRecruitStationPos().get().getX(),this.host.getRecruitStationPos().get().getY(),this.host.getRecruitStationPos().get().getZ())<=9){
            this.host.stopMovingtoRecruitStation();
        }

    }

    @Nullable
    protected Vector3d getPosition() {
        if(this.host.getRecruitStationPos().isPresent()) {
            return RandomPositionGenerator.getPosTowards(this.host, 10, 7, Vector3d.atCenterOf(this.host.getRecruitStationPos().get()));
        }
        else{
            return null;
        }
    }
}
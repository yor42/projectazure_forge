package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.mojang.math.Vector3d;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class CompanionMoveToRecruitStationGoal extends Goal {

    private final AbstractEntityCompanion host;
    Vec3 NextPos;

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
    protected Vec3 getPosition() {
        if(this.host.getRecruitStationPos().isPresent()) {
            return LandRandomPos.getPosTowards(this.host, 10, 7, Vec3.atCenterOf(this.host.getRecruitStationPos().get()));
        }
        else{
            return null;
        }
    }
}
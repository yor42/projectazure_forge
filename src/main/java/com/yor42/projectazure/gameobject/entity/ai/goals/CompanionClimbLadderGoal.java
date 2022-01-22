package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;

import java.util.EnumSet;
/*
 * This class is distributed as part of the ImprovedMobs Mod.
 * Get the Source Code in github:
 * https://github.com/Flemmli97/ImprovedMobs
 *
 * I wish could write my own but I have 0 idea how AI works. sorry Flemmli97 ;C
 */
public class CompanionClimbLadderGoal extends Goal {

    private final AbstractEntityCompanion entity;
    private Path path;

    public CompanionClimbLadderGoal(AbstractEntityCompanion entity){
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {

        if(this.entity.isCriticallyInjured()){
            return false;
        }

        if (!this.entity.getNavigation().isDone()) {
            this.path = this.entity.getNavigation().getPath();
            return this.path != null && this.entity.onClimbable();
        }
        return false;
    }

    @Override
    public void stop() {
        this.entity.isClimbingUp = false;
    }

    @Override
    public void tick() {
        int i = this.path.getNextNodeIndex();
        if (i + 1 < this.path.getNodeCount()) {
            int y = this.path.getNode(i).y;
            this.entity.setDeltaMovement(this.entity.getDeltaMovement().multiply(0, 0, 0));
            PathPoint pointNext = this.path.getNode(i + 1);
            BlockState down = this.entity.level.getBlockState(this.entity.blockPosition().below());
            double yMotion;
            if (pointNext.y < y || (pointNext.y == y && !down.getBlock().isLadder(down, this.entity.level,this.entity.blockPosition().below(), this.entity))) {
                yMotion = -0.15;
                this.entity.isClimbingUp = false;
            }
            else{
                yMotion = 0.15;
                this.entity.isClimbingUp = true;
            }
            this.entity.setDeltaMovement(this.entity.getDeltaMovement().multiply(0.1, 1, 0.1));
            this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(0, yMotion, 0));
        }
    }
}

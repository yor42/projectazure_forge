package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.misc.EntityMissileDrone;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static net.minecraft.world.entity.ai.goal.Goal.Flag.LOOK;
import static net.minecraft.world.entity.ai.goal.Goal.Flag.MOVE;


public class DroneRangedAttackGoal extends Goal {

    private final EntityMissileDrone drone;
    @Nullable
    private LivingEntity target;
    private int seeTime;
    private int rangedAttackTime;

    public DroneRangedAttackGoal(EntityMissileDrone drone) {
        this.drone = drone;
        this.rangedAttackTime = -1;
        setFlags(EnumSet.of(LOOK, MOVE));
    }

    @Override
    public boolean canUse() {

        boolean hastarget = this.drone.getTarget() != null;

        if(drone.isReturningToOwner()){
            return false;
        }

        if(hastarget&&this.drone.hasAmmo() && this.drone.getTarget().isAlive()) {
            this.target = this.drone.getTarget();
            return true;
        }
        return false;
    }


    @Override
    public void stop() {
        this.seeTime = 0;
        this.rangedAttackTime = -1;
        this.target = null;
    }

    public boolean canContinueToUse() {

        if(drone.isReturningToOwner()){
            return false;
        }

        return this.canUse() || !this.drone.getNavigation().isDone();
    }

    public void tick() {
        if (this.target != null) {
            boolean lvt_3_1_ = this.drone.getSensing().hasLineOfSight(this.target);
            if (lvt_3_1_) {
                this.drone.getLookControl().setLookAt(this.target, 10.0F, (float) this.drone.getMaxHeadXRot());
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }


            if (this.drone.distanceTo(this.target) < 6 && this.seeTime >= 5) {
                this.drone.getNavigation().stop();
            } else {
                this.drone.getNavigation().moveTo(this.target.getX(), this.target.getEyeY() + 0.5, this.target.getZ(), 1);
            }

            if (this.drone.distanceTo(this.target) <= 3.0D) {
                this.drone.getMoveControl().strafe(-1F, 0);
            }

            this.drone.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            if (--this.rangedAttackTime == 0) {
                if (!lvt_3_1_) {
                    return;
                }

                this.drone.FireMissile(this.target);
                this.rangedAttackTime = this.drone.getFiredelay();
            } else if (this.rangedAttackTime < 0) {
                this.rangedAttackTime = this.drone.getFiredelay();
            }
        }
    }

}

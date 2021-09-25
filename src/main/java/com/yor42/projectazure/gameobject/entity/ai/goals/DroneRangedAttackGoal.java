package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.misc.EntityMissileDrone;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static net.minecraft.entity.ai.goal.Goal.Flag.LOOK;
import static net.minecraft.entity.ai.goal.Goal.Flag.MOVE;

public class DroneRangedAttackGoal extends Goal {

    private final EntityMissileDrone drone;
    @Nullable
    private LivingEntity target;
    private int seeTime;
    private int rangedAttackTime;

    public DroneRangedAttackGoal(EntityMissileDrone drone) {
        this.drone = drone;
        this.rangedAttackTime = -1;
        setMutexFlags(EnumSet.of(LOOK, MOVE));
    }

    @Override
    public boolean shouldExecute() {

        boolean hastarget = this.drone.getAttackTarget() != null;

        if(drone.isReturningToOwner()){
            return false;
        }

        if(hastarget&&this.drone.hasAmmo() && this.drone.getAttackTarget().isAlive()) {
            this.target = this.drone.getAttackTarget();
            return true;
        }
        return false;
    }


    @Override
    public void resetTask() {
        this.seeTime = 0;
        this.rangedAttackTime = -1;
        this.target = null;
    }

    public boolean shouldContinueExecuting() {

        if(drone.isReturningToOwner()){
            return false;
        }

        return this.shouldExecute() || !this.drone.getNavigator().noPath();
    }

    public void tick() {
        if (this.target != null) {
            boolean lvt_3_1_ = this.drone.getEntitySenses().canSee(this.target);
            if (lvt_3_1_) {
                this.drone.getLookController().setLookPositionWithEntity(this.target, 10.0F, (float) this.drone.getVerticalFaceSpeed());
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }


            if (this.drone.getDistance(this.target) < 6 && this.seeTime >= 5) {
                this.drone.getNavigator().clearPath();
            } else {
                this.drone.getNavigator().tryMoveToXYZ(this.target.getPosX(), this.target.getPosYEye() + 0.5, this.target.getPosZ(), 1);
            }

            if (this.drone.getDistance(this.target) <= 3.0D) {
                this.drone.getMoveHelper().strafe(-1F, 0);
            }

            this.drone.getLookController().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
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

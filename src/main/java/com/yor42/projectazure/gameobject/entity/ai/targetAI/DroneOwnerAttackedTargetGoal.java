package com.yor42.projectazure.gameobject.entity.ai.targetAI;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityDrone;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;

import java.util.EnumSet;

public class DroneOwnerAttackedTargetGoal extends TargetGoal {
    private final AbstractEntityDrone Drone;
    private LivingEntity attacker;
    private int timestamp;

    public DroneOwnerAttackedTargetGoal(AbstractEntityDrone p_i1668_1_) {
        super(p_i1668_1_, false);
        this.Drone = p_i1668_1_;
        this.setMutexFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean shouldExecute() {
        if (this.Drone.getOwner().isPresent()) {
            LivingEntity lvt_1_1_ = (LivingEntity) this.Drone.getOwner().get();
            this.attacker = lvt_1_1_.getLastAttackedEntity();
            int lvt_2_1_ = lvt_1_1_.getLastAttackedEntityTime();
            return lvt_2_1_ != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT) && this.Drone.shouldAttackEntity(this.attacker, lvt_1_1_);
        } else {
            return false;
        }
    }

    public void startExecuting() {
        this.Drone.setAttackTarget(this.attacker);
        if (this.Drone.getOwner().isPresent()) {
            Entity owner = this.Drone.getOwner().get();
            if(owner instanceof LivingEntity) {
                this.timestamp = ((LivingEntity)this.Drone.getOwner().get()).getLastAttackedEntityTime();
            }
        }

        super.startExecuting();
    }

}
